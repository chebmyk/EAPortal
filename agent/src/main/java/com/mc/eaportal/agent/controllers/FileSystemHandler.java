package com.mc.eaportal.agent.controllers;

import com.mc.eaportal.agent.model.Node;
import com.mc.eaportal.agent.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileSystemHandler {

    @Value("${agent.log-path}")
    private String logPath;

    private FileService fileService;

    public FileSystemHandler(FileService fileService) {
        this.fileService = fileService;
    }

    public Mono<ServerResponse> getFileTree(ServerRequest serverRequest) {
        Node result = fileService.getFolderTree(logPath);
        return ServerResponse
                .ok()
                .body(Mono.just(result), Node.class);
    }

    public Mono<ServerResponse> fileStream(ServerRequest serverRequest) {
        log.info("Request: {}", serverRequest);
        Flux<String> lineStream =  serverRequest.bodyToMono(Map.class)
                    .flatMapMany(body -> {
                        String param = (String) body.get("file");
                        log.info("Reading file: " + param);
                        Path path = Paths.get(param);
                        return Flux.using(
                                () -> Files.lines(path),
                                Flux::fromStream,
                                Stream::close
                        )
                        .flatMap(Flux::just)
                        .doOnComplete(() -> log.info("Reading file: " + param + " completed"))
                        .log();
                    });
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(lineStream, String.class);
    }
}