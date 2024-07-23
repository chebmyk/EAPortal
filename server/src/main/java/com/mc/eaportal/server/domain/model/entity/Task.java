package com.mc.eaportal.server.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
///import org.springframework.data.annotation.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document
public class Task {
    //@Id
    private String id;
    private String name;
    private String description;
    private String body;
}
