package com.mc.eaportal.agent.controllers;

import com.mc.eaportal.agent.services.ZookeeperService;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("zookeeper")
public class ZookeeperController {

    private static final String SERVICE_NAME = "eagents";

    private ZookeeperService appZookeeperService;


    public ZookeeperController(ZookeeperService appZookeeperService) {
        this.appZookeeperService = appZookeeperService;
    }

    @GetMapping("instance")
    public ResponseEntity<Collection<ServiceInstance<ZookeeperInstance>>> getInstance() throws Exception {
       Collection<ServiceInstance<ZookeeperInstance>> result = appZookeeperService.getInstanceByName(SERVICE_NAME);
       return ResponseEntity.ok(result);
    }
}
