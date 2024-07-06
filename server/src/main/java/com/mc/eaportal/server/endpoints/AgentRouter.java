package com.mc.eaportal.server.endpoints;

import com.mc.eaportal.server.services.AgentService;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AgentRouter {

    @Autowired
    private AgentRequestHandler agentRequestHandler;

    @RouterOperations({
            @RouterOperation(path = "/agents", method = RequestMethod.GET, beanClass = AgentRequestHandler.class, beanMethod = "getAgents"),
            @RouterOperation(path = "/agents/{id}", method = RequestMethod.GET, beanClass = AgentRequestHandler.class, beanMethod = "getAgent"),
            @RouterOperation(path = "/agents", method = RequestMethod.POST, beanClass = AgentRequestHandler.class, beanMethod = "createAgent"),
            @RouterOperation(path = "/agents/{id}", method = RequestMethod.PUT, beanClass = AgentRequestHandler.class, beanMethod = "updateAgent"),
            @RouterOperation(path = "/agents/{id}", method = RequestMethod.DELETE, beanClass = AgentRequestHandler.class, beanMethod = "deleteAgent"),
            @RouterOperation(path = "/agents/generate", method = RequestMethod.GET, beanClass = AgentRequestHandler.class, beanMethod = "generate")
    }
    )
    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions
                .route(GET("/agents"), agentRequestHandler::getAgents)
                .andRoute(GET("/agents/{id}"), agentRequestHandler::getAgent)
                .andRoute(POST("/agents"), agentRequestHandler::createAgent)
                .andRoute(PUT("/agents/{id}"), agentRequestHandler::updateAgent)
                .andRoute(DELETE("/agents/{id}"), agentRequestHandler::deleteAgent)
                .andRoute(GET("/agents/generate"), agentRequestHandler::generate);

    }
}
