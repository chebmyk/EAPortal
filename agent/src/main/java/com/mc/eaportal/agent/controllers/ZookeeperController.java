package com.mc.eaportal.agent.controllers;

import com.mc.eaportal.agent.services.AppZookeeperService;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping("zookeeper")
public class ZookeeperController {

    private AppZookeeperService appZookeeperService;


    public ZookeeperController(AppZookeeperService appZookeeperService) {
        this.appZookeeperService = appZookeeperService;
    }

    @GetMapping("instance")
    public ResponseEntity<Collection<ServiceInstance<ZookeeperInstance>>> getInstance() throws Exception {
       Collection<ServiceInstance<ZookeeperInstance>> result = appZookeeperService.getInstanceByName("eagents");
       return ResponseEntity.ok(result);
    }
}
