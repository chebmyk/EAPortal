package com.mc.eaportal.server.endpoints;

import com.mc.eaportal.server.domain.model.FileAttributes;
import com.mc.eaportal.server.domain.model.entity.Node;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromServerSentEvents;

@Slf4j
@Component
public class ZookeeperRequestHandler {


    @Autowired
    private ZookeeperService zookeeperService;


    @Operation(summary = "Get Current Zookeeper Instances")
    public Mono<ServerResponse> getInstances(ServerRequest serverRequest) {
        Flux<ServiceInstance> agents = zookeeperService.getInstances().log();
        return ServerResponse.ok()
                .body(agents, ServiceInstance.class);
    }


    public Mono<ServerResponse> getInstance(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");
        Mono<ServiceInstance> instance = zookeeperService.getInstances()
                .filter(i -> i.getInstanceId().equals(agentId))
                .last();
        return  ServerResponse.ok()
                .body(instance, ServiceInstance.class);
    }


    public Mono<ServerResponse> systemLoadStream(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");
        Flux<ServerSentEvent<String>> sse = getWebClient(agentId).flatMapMany( webClient ->
                webClient.get()
                .uri("/stream/systemload")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .log()
        );
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(fromServerSentEvents(sse));
    }


    public Mono<ServerResponse> fileStream(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");

        Flux<String> fileStream = serverRequest
                 .bodyToMono(Map.class)
                 .flatMapMany( body -> {
                                String filePath =String.valueOf(body.get("file"));
                                log.info("filePath: {}", filePath);

                                return getWebClient(agentId).flatMapMany( webClient ->
                                        webClient.post()
                                        .uri("/fs/filestream")
                                        //.header("Content-Type","application/json")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(body))
                                        .accept(MediaType.TEXT_EVENT_STREAM)
                                        .retrieve()
                                        .bodyToFlux(String.class)
                                        .log()
                                )
                                ;
                            }
                        );


        return  ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(fileStream, String.class);

    }


    public Mono<ServerResponse> getFileTree(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");
        Mono<Node> treeNode = getWebClient(agentId).flatMap( webClient ->
                webClient
                .get()
                .uri("/fs/filetree")
                .retrieve()
                .bodyToMono(Node.class)
                .log()
                );
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(treeNode, Node.class);
    }


    public Mono<ServerResponse> fileMetaData(ServerRequest serverRequest) {
        String agentId = serverRequest.pathVariable("id");
        Mono<FileAttributes> fileMetaData =serverRequest
                .bodyToMono(Map.class)
                .flatMap( body -> {
                            String filePath =String.valueOf(body.get("file"));
                            log.info("filePath: {}", filePath);
                            return getWebClient(agentId).flatMap( webClient ->
                                    webClient.post()
                                    .uri("/fs/filemetadata")
                                    //.header("Content-Type","application/json")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(BodyInserters.fromValue(body))
                                    .retrieve()
                                    .bodyToMono(FileAttributes.class)
                                    .log()
                            );
                        }
                );

        return  ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileMetaData, FileAttributes.class);
    }

    private Mono<WebClient> getWebClient(String agentId) {
        return getAgentUrl(agentId).flatMap(url ->
                Mono.just(
                        WebClient.builder()
                                .baseUrl(url)
                                .clientConnector(new ReactorClientHttpConnector(
                                        HttpClient.create()
                                                .resolver(DefaultAddressResolverGroup.INSTANCE) //Custom DNS Resolvr Section 4.13 https://projectreactor.io/docs/netty/release/reference/index.html
                                )
                                )
                                .build()
                )

        );
    }

    private Mono<String> getAgentUrl(String agentId) {
        return zookeeperService.getInstances()
                .filter(i -> i.getInstanceId().equals(agentId))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .switchIfEmpty(Mono.error(new RuntimeException("AgentID ["+agentId+"] no longer exists")))
                .last();
    }
}
