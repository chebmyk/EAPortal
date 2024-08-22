package com.mc.eaportal.server.domain.model;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class FileAttributes {
    LocalDateTime lastModifiedTime;
    LocalDateTime lastAccessTime;
    LocalDateTime creationTime;
    boolean regularFile;
    boolean directory;
    boolean symbolicLink;
    long size;
}
