package com.mc.eaportal.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServiceInstance;
import org.springframework.cloud.zookeeper.discovery.reactive.ZookeeperReactiveDiscoveryClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ZookeeperService {
    private static final String SERVICE_NAME = "eagents";

    @Autowired
    private ZookeeperReactiveDiscoveryClient zookeeperClient;

    public Flux<ServiceInstance> getInstances() {
        return zookeeperClient.getInstances(SERVICE_NAME);
    }
}


