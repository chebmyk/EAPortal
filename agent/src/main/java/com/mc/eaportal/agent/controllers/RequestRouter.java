package com.mc.eaportal.agent.controllers;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@CrossOrigin
@Component
public class RequestRouter {

    @Autowired
    private FileSystemHandler fileSystemHandler;
    @Autowired
    private StreamHandler streamHandler;


    @RouterOperations({
            @RouterOperation(path = "/fs/filestream",
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                    method = RequestMethod.POST,
                    beanClass = FileSystemHandler.class,
                    beanMethod = "fileStream"),
            @RouterOperation(path = "/fs/filetree",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = FileSystemHandler.class,
                    beanMethod = "getFileTree"),
            @RouterOperation(path = "/fs/filemetadata",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = FileSystemHandler.class,
                    beanMethod = "getFileMetadata"),
    }
    )
    @Bean
    public RouterFunction<ServerResponse> fsRoutes() {
        return RouterFunctions
                .route(POST("/fs/filestream"), fileSystemHandler::fileStream)
                .andRoute(GET("/fs/filetree"), fileSystemHandler::getFileTree)
                .andRoute(POST("/fs/filemetadata"), fileSystemHandler::getFileMetadata)
                ;
    }

    @RouterOperations({
            @RouterOperation(path = "/stream/systemload",
                    produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                    method = RequestMethod.GET,
                    beanClass = StreamHandler.class,
                    beanMethod = "memoryStream"),
    }
    )
    @Bean
    public RouterFunction<ServerResponse> sseRoutes() {
        return RouterFunctions
                .route(GET("/stream/systemload"), streamHandler::systemLoadStream)
                ;
    }
}
