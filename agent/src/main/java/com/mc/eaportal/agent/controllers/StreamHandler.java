package com.mc.eaportal.agent.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import oshi.SystemInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromServerSentEvents;

@Slf4j
@Component
public class StreamHandler {

    @Autowired
    private SystemInfo systemInfo;

    public Mono<ServerResponse> systemLoadStream(ServerRequest serverRequest) {

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

        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(fromServerSentEvents(combinedStream));
    }

}
