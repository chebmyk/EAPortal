package com.mc.eaportal.server.endpoints;

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

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromServerSentEvents;

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
        Flux<ServerSentEvent<Long>> cpuStream = Flux.interval(Duration.ofSeconds(1))
                .map(i -> systemInfo.getHardware().getMemory().getAvailable())
                .map(mem -> ServerSentEvent.<Long>builder().data(mem).build());
        return ServerResponse.ok().body(fromServerSentEvents(cpuStream));
    }
}
