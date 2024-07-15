package com.mc.eaportal.agent.config;


import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.ServiceType;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;

@Configuration
public class AgentConfig {

    @Bean
    public ZookeeperRegistration getZookeeperRegistration(ZookeeperServiceRegistry serviceRegistry) {

        return  ServiceInstanceRegistration.builder()
                .name("eagents")
                .serviceType(ServiceType.DYNAMIC)
                .address("localhost") //todo review the address
                //.payload(new ZookeeperInstance())
                .build();

    }


    @Bean
    public SystemInfo systemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo;
    }
}
