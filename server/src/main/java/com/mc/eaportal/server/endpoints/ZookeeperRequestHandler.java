package com.mc.eaportal.server.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import oshi.SystemInfo;
import com.mc.eaportal.server.services.ZookeeperService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.BodyInserters.fromServerSentEvents;
@Slf4j
@Component
public class ZookeeperRequestHandler {

    @Autowired
    private ZookeeperService zookeeperService;

    @Autowired
    private SystemInfo systemInfo;


    @Operation(summary = "Get Current Zookeeper Instances")
    public Mono<ServerResponse> getInstances(ServerRequest serverRequest) {
        Flux<ServiceInstance> agents = zookeeperService.getInstances().log();
        return ServerResponse.ok().body(agents, ServiceInstance.class);
    }


    public Mono<ServerResponse> getInstance(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");
        Mono<ServiceInstance> instance = zookeeperService.getInstances()
                .filter(i -> i.getInstanceId().equals(agentId))
                .last();
        return  ServerResponse.ok().body(instance, ServiceInstance.class);
    }

    public Mono<ServerResponse> memoryStream(ServerRequest serverRequest) {

        Flux<ServerSentEvent<String>> memoryStream = Flux.interval(Duration.ofSeconds(2))
                .map(i ->
                        String.valueOf(systemInfo.getHardware().getMemory().getAvailable())
                )
                .map(mem -> ServerSentEvent.<String>builder()
                        .id("memory")
                        .data(mem).build()
                );

        Flux<ServerSentEvent<String>> cpuStream = Flux.interval(Duration.ofSeconds(2))
                .map(i -> String.valueOf(systemInfo.getHardware().getProcessor().getSystemCpuLoad(2000)))
                .map(cpu -> ServerSentEvent.<String>builder()
                        .id("cpu")
                        .data(cpu).build()
                );

        Flux<ServerSentEvent<String>> combinedStream = Flux.merge(memoryStream, cpuStream)
                .flatMap(Flux::just)
                .log();

        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(fromServerSentEvents(combinedStream));
    }

    public Mono<ServerResponse> fileStream(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Map.class)
                .flatMap(body -> {
                    String filePath = (String) body.get("file");
                    log.info("Reading file: " + filePath);
                    Path path = Paths.get(filePath);
                    Flux<String> fileStream = Flux.using(
                            () -> Files.lines(path),
                            Flux::fromStream,
                            Stream::close
                    ).flatMap(Flux::just).log();
                    return ServerResponse
                            .ok()
                            .contentType(MediaType.TEXT_EVENT_STREAM)
                            .body(fileStream, String.class);
                });
    }
}
