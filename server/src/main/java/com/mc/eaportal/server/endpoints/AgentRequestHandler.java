package com.mc.eaportal.server.endpoints;

import com.mc.eaportal.server.domain.model.entity.Agent;
import com.mc.eaportal.server.services.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AgentRequestHandler {

    @Autowired
    private AgentService agentService;


    @Operation(summary = "Get All Agents")
    public Mono<ServerResponse> getAgents(ServerRequest request) {
        Flux<Agent> agents = agentService.getAgents();
        return ServerResponse.ok().body(agents, Agent.class);
    }


    public Mono<ServerResponse> getAgent(ServerRequest request) {
        String agentId = request.pathVariable("id");
        Mono<Agent> userMono = agentService.getAgent(agentId);
        return userMono.flatMap(agent -> ServerResponse.ok().bodyValue(agent))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> createAgent(ServerRequest request) {
        Mono<Agent> userMono = request.bodyToMono(Agent.class);
        return userMono.flatMap(agent -> agentService.save(agent))
                .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser));
    }


    public Mono<ServerResponse> updateAgent(ServerRequest request) {
        String agentId = request.pathVariable("id");
        Mono<Agent> userMono = request.bodyToMono(Agent.class);
        return userMono.flatMap(agent -> {
                    agent.setId(agentId);
                    return agentService.save(agent);
                }).flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    public Mono<ServerResponse> deleteAgent(ServerRequest request) {
        String agentId = request.pathVariable("id");
        return agentService.deleteAgent(agentId)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

//    public Mono<ServerResponse> generate(ServerRequest request) {
//        Flux<Agent> agents = agentService.generateAgent();
//        return ServerResponse.ok().body(agents, Agent.class);
//    }


}
