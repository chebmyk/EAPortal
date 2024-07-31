package com.mc.eaportal.agent.services;

import com.mc.eaportal.agent.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.stream.BaseStream;

@Service
@Slf4j
public class FileService {

    public Node getFolderTree(String directory) {
        log.info("Reading directory: " + directory);
        try (Stream<Path> paths = Files.walk(Paths.get(directory),1)) {
            Node node = new Node(
                    getDirectoryName(directory),
                    true,
                    directory,
                    new HashSet<>()
            );

            paths.skip(1).forEach(path -> {
                log.info(path.toString());
                if (Files.isDirectory(path)) {
                    node.getChildren().add(getFolderTree(path.toString()));
                } else {
                    Node child = new Node(
                        path.getFileName().toString(),
                        false,
                        path.toString(),
                        null
                    );
                    node.getChildren().add(child);
                }
            });
            return node;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading directory: " + directory, e);
        }
    }

    public Flux<String> readFileToFlux(String filePath) {
        log.info("Reading file: " + filePath);
        Path path = Paths.get(filePath);
            return Flux.using(
                    () -> Files.lines(path),
                    Flux::fromStream,
                    Stream::close
            );
    }

    public String getDirectoryName(String path) {
        Path directoryPath = Paths.get(path);
        return directoryPath.getFileName().toString();
    }
}
