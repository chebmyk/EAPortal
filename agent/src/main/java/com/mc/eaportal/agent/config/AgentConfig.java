package com.mc.eaportal.agent.config;


import org.apache.curator.x.discovery.ServiceType;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oshi.SystemInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AgentConfig {

    @Bean
    public ZookeeperRegistration getZookeeperRegistration(ApplicationContext context, ZookeeperServiceRegistry serviceRegistry) throws UnknownHostException {


        SystemInfo systemInfo = new SystemInfo();
        //systemInfo.getOperatingSystem().getNetworkParams().getHostName()



        Map<String, String> properties = new HashMap<>();
        properties.putIfAbsent("os.name", System.getProperty("os.name", "N/A"));
        properties.putIfAbsent("os.arch", System.getProperty("os.arch", "N/A"));
        properties.putIfAbsent("os.version", System.getProperty("os.version", "N/A"));

        properties.putIfAbsent("cpu.name", systemInfo.getHardware().getProcessor().getProcessorIdentifier().getName());
        properties.putIfAbsent("cpu.frequency", String.valueOf(systemInfo.getHardware().getProcessor().getMaxFreq()));
        properties.putIfAbsent("cpu.model", String.valueOf(systemInfo.getHardware().getProcessor().getProcessorIdentifier().getModel()));
        properties.putIfAbsent("cpu.cores", String.valueOf(systemInfo.getHardware().getProcessor().getPhysicalProcessorCount()));

        properties.putIfAbsent("memory.available", String.valueOf(systemInfo.getHardware().getMemory().getAvailable()));
        properties.putIfAbsent("memory.total", String.valueOf(systemInfo.getHardware().getMemory().getTotal()));

        String hostname = InetAddress.getLocalHost().getCanonicalHostName();

        ZookeeperInstance zookeeperInstance = new ZookeeperInstance(context.getId(),
                context.getApplicationName(), properties);


        return  ServiceInstanceRegistration.builder()
                .name("eagents")
                .serviceType(ServiceType.DYNAMIC)
                .address(hostname)
                .payload(zookeeperInstance)
                .defaultUriSpec()
                .build();

    }


    @Bean
    public SystemInfo systemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo;
    }
}
