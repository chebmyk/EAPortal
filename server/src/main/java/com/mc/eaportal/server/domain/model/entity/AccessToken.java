package com.mc.eaportal.server.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document
public class AccessToken {
    //@Id
    private String id;
    private String alias;
    private String token;
    private String validFom;
    private String validTo;
}
