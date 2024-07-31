package com.mc.eaportal.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Node {
    private String name;
    private boolean isDirectory;
    private String path;
    private Set<Node> children;
}
