package com.mc.eaportal.server.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Node {
    private String name;
    private boolean directory;
    private String path;
    private Set<Node> children;
}
