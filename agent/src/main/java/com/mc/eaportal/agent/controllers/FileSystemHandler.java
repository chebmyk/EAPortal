package com.mc.eaportal.agent.controllers;

import com.google.common.collect.Lists;
import com.mc.eaportal.agent.model.FileAttributes;
import com.mc.eaportal.agent.model.Node;
import com.mc.eaportal.agent.services.FileService;
import com.mc.eaportal.agent.services.LogParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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


    public Mono<ServerResponse> getFileMetadata(ServerRequest serverRequest) {
        Mono<FileAttributes> fileAttributes = serverRequest.bodyToMono(Map.class)
                .flatMap(body -> {
                            String file = (String) body.get("file");
                            try {
                                return Mono.just(fileService.getFileMetaData(file));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileAttributes, FileAttributes.class);
    }


    public Mono<ServerResponse> fileStream(ServerRequest serverRequest) {

        final AtomicLong linesTotal = new AtomicLong();
        LogParserService parserService = new LogParserService();
        Map<String, List<Long>> parseResult = new ConcurrentHashMap<>();

        Mono<String> errors = Mono.just("::Parse Result:" + parseResult.toString());

        Mono<String> readRequest =  serverRequest.bodyToMono(Map.class)
                .flatMap(body -> {
                            String file = (String) body.get("file");
                            return Mono.just(file);
                        }
                );

        Flux<String> lineStream = readRequest
                .flatMapMany(fileService::readFileToFlux)
                .flatMap(line -> {
                    log.info("total lines {}", linesTotal.incrementAndGet());
                    parserService.parse(line).forEach( parser -> {
                        if(parseResult.containsKey(parser)) {
                            parseResult.get(parser).add(linesTotal.get());
                        }
                            parseResult.putIfAbsent(parser, Lists.newArrayList(linesTotal.get()));
                        }
                    );
                    log.info("Parse {}", parseResult.toString());
                    return Mono.just(line);
                })
                .log()
                .concatWith(errors)
                ;

        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(lineStream, String.class);
    }
}