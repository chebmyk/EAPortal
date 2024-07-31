package com.mc.eaportal.agent.controllers;

import com.mc.eaportal.agent.model.Node;
import com.mc.eaportal.agent.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("fs")
public class FileSystemController {

    private FileService fileService;

    public FileSystemController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("filetree")
    public ResponseEntity<Node> getInstance(String path) throws Exception {
        path="/Users/michael/Documents/Projects/Java/EAPortal/agent/src/main/java/com/mc/eaportal/agent";
        Node result = fileService.getFolderTree(path);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "file")
    public Mono<ServerResponse> getFile(@RequestBody Map<String, String> file) throws Exception {
        Flux<String> result =  fileService.readFileToFlux(file.get("file"));
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(result, String.class);
    }
}
