package com.mc.eaportal.agent.services;

import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;

import java.util.Collection;

public interface AppZookeeperService {
    Collection<ServiceInstance<ZookeeperInstance>> getInstanceByName(String serviceName) throws Exception;
}
