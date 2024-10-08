package com.mc.eaportal.agent.services;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    private CuratorFramework curatorFramework;
    private ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    private ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    public ZookeeperServiceImpl(CuratorFramework curatorFramework, ZookeeperDiscoveryProperties zookeeperDiscoveryProperties) {
        this.curatorFramework = curatorFramework;
        this.zookeeperDiscoveryProperties = zookeeperDiscoveryProperties;
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ZookeeperInstance.class)
                .client(curatorFramework)
                .basePath(zookeeperDiscoveryProperties.getRoot())
                .build();
    }

    public Collection<ServiceInstance<ZookeeperInstance>>  getInstanceByName(String serviceName) throws Exception {
            return serviceDiscovery.queryForInstances(serviceName);
    }

}
