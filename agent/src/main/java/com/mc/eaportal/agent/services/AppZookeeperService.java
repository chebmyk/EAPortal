package com.mc.eaportal.agent.services;

import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;

public interface AppZookeeperService {
    ServiceInstance<ZookeeperInstance> getInstanceByName(String serviceName);
}
