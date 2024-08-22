package com.mc.eaportal.agent.services;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


@Slf4j
public class LogParserService {

//    declare -r DMESG_REGEX_EMERGENCY=".*(:emerg :).*"
//    declare -r DMESG_REGEX_ALERT=".*(:alert :).*"
//    declare -r DMESG_REGEX_CRITICAL=".*(:crit  :).*"
//    declare -r DMESG_REGEX_ERROR=".*(:err   :).*"
//    declare -r DMESG_REGEX_WARNING=".*(:warn  :).*"
//    declare -r DMESG_REGEX_CPU_STUCK=".*(soft lockup - CPU.* stuck for .*s).*"
//    declare -r DMESG_TASK_BLOCKED=".*(task .* blocked for more than .* seconds).*"
//    declare -r DMESG_NFS_TIMEOUT=".*(nfs: server .* not responding, timed out).*"


    private Map<String, String> parsers;

    public LogParserService() {
        parsers = new ConcurrentHashMap<>();
        parsers.putIfAbsent("INFO", ".*(: INFO ).*");
        parsers.putIfAbsent("WARNING", ".*(:warn   :).*");
        parsers.putIfAbsent("ERROR", ".*(:err   :).*");
    }


    public List<String> parse(String line) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : parsers.entrySet()) {
            String patternValue = entry.getValue();
            if (Pattern.matches(patternValue, line)) {
                log.info(entry.getKey() + " Parser detected for : {}", line);
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
