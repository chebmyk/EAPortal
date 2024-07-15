package com.mc.eaportal.server.domain.model.entity;

import lombok.Data;

@Data
public class Response {
    private String requestId;
    private String status;
    private String body;
}
