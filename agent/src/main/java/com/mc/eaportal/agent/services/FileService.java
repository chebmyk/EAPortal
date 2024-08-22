package com.mc.eaportal.agent.services;

import com.mc.eaportal.agent.model.FileAttributes;
import com.mc.eaportal.agent.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            paths.skip(1).forEach(path -> { //todo review skip logic for root folder
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
            )
            .flatMap(Flux::just)
            .doOnComplete(() -> log.info("Reading file: " + filePath + " completed"));
    }


    public FileAttributes getFileMetaData(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
        FileAttributes fileAttributes = new FileAttributes();
        fileAttributes.setSize(attributes.size());
        fileAttributes.setCreationTime(LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault()));
        fileAttributes.setLastAccessTime(LocalDateTime.ofInstant(attributes.lastAccessTime().toInstant(), ZoneId.systemDefault()));
        fileAttributes.setLastModifiedTime(LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
        fileAttributes.setDirectory(attributes.isDirectory());
        return fileAttributes;
    }



    public String getDirectoryName(String path) {
        Path directoryPath = Paths.get(path);
        return directoryPath.getFileName().toString();
    }
}
