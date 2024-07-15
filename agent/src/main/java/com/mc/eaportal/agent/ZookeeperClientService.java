package com.mc.eaportal.agent;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;

import java.io.IOException;
import java.net.InetAddress;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;


//@Component
//@Slf4j
public class ZookeeperClientService {

    private ZooKeeper zooKeeper;
    private SystemInfo systemInfo;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final static String agentsPath = "/eagents";

    @Value("${zookeeper.host:localhost}")
    private String zookeeperHost;
    @Value("${zookeeper.port:2181}")
    private int zookeeperPort;
    private int zookeeperSessionTimeout = 30_000;

    public ZookeeperClientService(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    @PostConstruct
    public void init() throws InterruptedException, KeeperException, IOException {
        if (getZooKeeper().exists(agentsPath, false) == null) {
            getZooKeeper().create(agentsPath, new byte[0], OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        register();
    }

    public void register () throws InterruptedException, KeeperException, JsonProcessingException {

        //log.info("System info: {}", objectMapper.writeValueAsString(systemInfo));
        String agentPath = String.join("/",agentsPath, systemInfo.getHardware().getComputerSystem().getHardwareUUID());
        try {
            getZooKeeper().create(agentPath, objectMapper.writeValueAsBytes(systemInfo.getOperatingSystem().getVersionInfo() ), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //generate method which returns system info os name ip hostname
    public SystemInfo systemInfo2() {
    return null;
//new                new AgentSystemInfo(
//                System.getProperty("os.name"),
//                InetAddress.getLoopbackAddress().getHostAddress(),
//                InetAddress.getLoopbackAddress().getHostName())

//        // Local address
//        InetAddress.getLocalHost().getHostAddress();
//        InetAddress.getLocalHost().getHostName();
//        // Remote address
//        InetAddress.getLoopbackAddress().getHostAddress();
//        InetAddress.getLoopbackAddress().getHostName();
    }

    private ZooKeeper getZooKeeper() throws IOException {
        return new ZooKeeper("localhost:2181", zookeeperSessionTimeout, null);
    }
}
