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

    @RouterOperations({
            @RouterOperation(path = "/zookeeper/instance", method = RequestMethod.GET, beanClass = ZookeeperRequestHandler.class, beanMethod = "getInstansces"),
            @RouterOperation(path = "/zookeeper/instance/{id}", method = RequestMethod.GET, beanClass = ZookeeperRequestHandler.class, beanMethod = "getInstance"),
            @RouterOperation(path = "/zookeeper/stream/cpu", produces = {MediaType.TEXT_EVENT_STREAM_VALUE} , method = RequestMethod.GET, beanClass = ZookeeperRequestHandler.class, beanMethod = "cpuStream"),

    }
    )
    @Bean
    public RouterFunction<ServerResponse> zkInstanceRoutes() {
        return RouterFunctions
                .route(GET("/zookeeper/instance"), zookeeperRequestHandler::getInstances)
                .andRoute(GET("/zookeeper/instance/{id}"), zookeeperRequestHandler::getInstance)
                .andRoute(GET("/zookeeper/stream/memory"), zookeeperRequestHandler::memoryStream)
                ;

    }

}
