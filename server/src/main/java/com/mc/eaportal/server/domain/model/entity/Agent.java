package com.mc.eaportal.server.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document
public class Agent {
    //@Id
    private String id;
    private String name;
    private String hostname;
    private String status;
    private String description;
}
