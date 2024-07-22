package com.mc.eaportal.agent.services;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherAutoConfiguration;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class PropertyWatcher implements DependencyWatcherListener {

    private DiscoveryClient discoveryClient;

    public PropertyWatcher(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    @Override
    public void stateChanged(String dependencyName, DependencyState newState) {
        System.out.println("Change detected: " + dependencyName);
        System.out.println("new DependencyState" + newState.name());
    }
}
