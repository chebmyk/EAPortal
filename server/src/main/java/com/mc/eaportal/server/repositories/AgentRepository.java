package com.mc.eaportal.server.repositories;

import com.mc.eaportal.server.domain.model.entity.Agent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends ReactiveCrudRepository<Agent, String> {

    
}
