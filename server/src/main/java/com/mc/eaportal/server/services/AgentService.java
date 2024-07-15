package com.mc.eaportal.server.services;

import com.mc.eaportal.server.domain.model.entity.Agent;
import com.mc.eaportal.server.repositories.AgentRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgentService {

    private AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }


    public Flux<Agent> getAgents() {
        return agentRepository.findAll();
    }

    public Mono<Agent> save(Agent agent) {
        return  agentRepository.save(agent);
    }

    public Mono<?> deleteAgent(String agentId) {
        return  agentRepository.deleteById(agentId);
    }

    public Mono<Agent> getAgent(String agentId) {
        return agentRepository.findById(agentId);
    }


    public Flux<Agent> generateAgent() {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Agent agent = new Agent();
            agent.setId("agentId" + i);
            agent.setName("agentName" + i);
            agent.setHostname("localhost");
            agent.setStatus("agentStatus");
            agent.setDescription("agentDescription" + i);
            agents.add(agent);
        }
        return agentRepository.saveAll(agents);
    }
}
