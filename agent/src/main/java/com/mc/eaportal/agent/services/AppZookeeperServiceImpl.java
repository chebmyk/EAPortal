package com.mc.eaportal.agent.services;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.stereotype.Service;

@Service
public class AppZookeeperServiceImpl  implements AppZookeeperService {
    private CuratorFramework curatorFramework;
    private ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    private ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    public AppZookeeperServiceImpl(CuratorFramework curatorFramework, ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        this.curatorFramework = curatorFramework;
        this.zookeeperDiscoveryProperties = zookeeperDiscoveryProperties;
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ZookeeperInstance.class)
                .client(curatorFramework)
                .basePath(zookeeperDiscoveryProperties.getRoot())
                .build();
    }

    public ServiceInstance<ZookeeperInstance> getInstanceByName(String serviceName) {
        try {
            return serviceDiscovery.queryForInstances(serviceName).stream()
                    .filter(i -> i.getName().equals(serviceName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
