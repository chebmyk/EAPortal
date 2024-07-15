package com.mc.eaportal.agent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class AgentSystemInfo {
    private String osName;
    @NonNull
    private String ip;
    @NonNull
    private String hostname;
}
