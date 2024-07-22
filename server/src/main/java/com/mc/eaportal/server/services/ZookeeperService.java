package com.mc.eaportal.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServiceInstance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZookeeperService {
    @Autowired
    private DiscoveryClient discoveryClient;

    public List<ServiceInstance> getAgents() {
        return discoveryClient.getInstances("eagents");
    }
}


