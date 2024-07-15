package com.mc.eaportal.server.domain.model.entity;

import lombok.Data;

@Data
public class Request {
    private String id;
    private Task task;
    private String agentId;
    private String status;
}
