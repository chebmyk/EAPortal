package com.mc.eaportal.server.endpoints;


import com.mc.eaportal.server.domain.model.entity.Agent;
import com.mc.eaportal.server.services.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("api/agent")
public class AgentRestController {
    @Autowired
    private AgentService agentService;


    @GetMapping
    public Mono<ServerResponse> getAgents() {
        Flux<Agent> agents = agentService.getAgents();
        return ServerResponse.ok().body(agents, Agent.class);
    }

    @GetMapping("/{agentId}")
    public Mono<ServerResponse> getAgent(@PathVariable String agentId) {
        Mono<Agent> userMono = agentService.getAgent(agentId);
        return userMono.flatMap(agent -> ServerResponse.ok().bodyValue(agent))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
//
//
//    public Mono<ServerResponse> createAgent(ServerRequest request) {
//        Mono<Agent> userMono = request.bodyToMono(Agent.class);
//        return userMono.flatMap(agent -> agentService.save(agent))
//                .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser));
//    }
//
//
//    public Mono<ServerResponse> updateAgent(ServerRequest request) {
//        String agentId = request.pathVariable("id");
//        Mono<Agent> userMono = request.bodyToMono(Agent.class);
//        return userMono.flatMap(agent -> {
//                    agent.setId(agentId);
//                    return agentService.save(agent);
//                }).flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser))
//                .switchIfEmpty(ServerResponse.notFound().build());
//    }
//
//
//    public Mono<ServerResponse> deleteAgent(ServerRequest request) {
//        String agentId = request.pathVariable("id");
//        return agentService.deleteAgent(agentId)
//                .then(ServerResponse.ok().build())
//                .switchIfEmpty(ServerResponse.notFound().build());
//    }
}
