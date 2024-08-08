package com.mc.eaportal.server.endpoints;

import com.mc.eaportal.server.domain.model.entity.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
import java.util.Map;

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

    public Mono<ServerResponse> systemLoadStream(ServerRequest serverRequest) {
        Flux<ServerSentEvent<String>> sse =getWebClient()
                .get()
                .uri("/stream/systemload")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .log();
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(fromServerSentEvents(sse));
    }



    public Mono<ServerResponse> fileStream(ServerRequest serverRequest) {

        Flux<String> fileStream =serverRequest
                 .bodyToMono(Map.class)
                 .flatMapMany( body -> {
                                String filePath =String.valueOf(body.get("file"));
                                log.info("filePath: {}", filePath);

                                return getWebClient()
                                        .post()
                                        .uri("/fs/filestream")
                                        .header("Content-Type","application/json")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(body))
                                        .accept(MediaType.TEXT_EVENT_STREAM)
                                        .retrieve()
                                        .bodyToFlux(String.class)
                                        .log()
                                ;
                            }
                        );


        return  ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(fileStream, String.class);

    }


    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081") // todo read from ZookeeperInstance
                .build();
    }

    public Mono<ServerResponse> getFileTree(ServerRequest serverRequest) {
        Mono<Node> treeNode =getWebClient()
                .get()
                .uri("/fs/filetree")
                .retrieve()
                .bodyToMono(Node.class)
                .log();
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(treeNode, Node.class);
    }
}
