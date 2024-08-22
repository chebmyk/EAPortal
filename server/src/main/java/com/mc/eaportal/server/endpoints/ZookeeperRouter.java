package com.mc.eaportal.server.endpoints;

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

import java.util.Arrays;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@CrossOrigin
@Component
public class ZookeeperRouter {
    @Autowired
    private ZookeeperRequestHandler zookeeperRequestHandler;

    @RouterOperations(
            {
            @RouterOperation(path = "/zookeeper/instance",
                    method = RequestMethod.GET,
                    beanClass = ZookeeperRequestHandler.class, beanMethod = "getInstansces"),
            @RouterOperation(path = "/zookeeper/instance/{id}",
                    method = RequestMethod.GET,
                    beanClass = ZookeeperRequestHandler.class, beanMethod = "getInstance"),
            @RouterOperation(path = "/stream/{id}/systemload",
                    produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                    method = RequestMethod.GET,
                    beanClass = ZookeeperRequestHandler.class, beanMethod = "systemLoadStream"),
            @RouterOperation(path = "/fs/{id}/filetree",
                            produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                            method = RequestMethod.GET,
                            beanClass = ZookeeperRequestHandler.class, beanMethod = "getFileTree"),
            @RouterOperation(path = "/fs/{id}/filestream",
                    produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                    method = RequestMethod.POST,
                    beanClass = ZookeeperRequestHandler.class, beanMethod = "fileStream"),
            @RouterOperation(path = "/fs/{id}/filemetadata",
                    produces = {MediaType.TEXT_EVENT_STREAM_VALUE},
                    method = RequestMethod.POST,
                    beanClass = ZookeeperRequestHandler.class, beanMethod = "fileMetaData"),
            }
    )

    @Bean
    public RouterFunction<ServerResponse> zkInstanceRoutes() {
        return RouterFunctions
                .route(GET("/zookeeper/instance"), zookeeperRequestHandler::getInstances)
                .andRoute(GET("/zookeeper/instance/{id}"), zookeeperRequestHandler::getInstance)
                .andRoute(GET("/stream/{id}/systemload"), zookeeperRequestHandler::systemLoadStream)
                .andRoute(GET("/fs/{id}/filetree"), zookeeperRequestHandler::getFileTree)
                .andRoute(POST("/fs/{id}/filestream"), zookeeperRequestHandler::fileStream)
                .andRoute(POST("/fs/{id}/filemetadata"), zookeeperRequestHandler::fileMetaData)
                ;
    }

}
