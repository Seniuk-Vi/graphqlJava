package org.brain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class File {
    private String id;
    private String folderId;
    private String name;
    private int size;
    private String description;

    public File(String folderId, String name, int size, String description) {
        this.id = UUID.randomUUID().toString();
        this.folderId = folderId;
        this.name = name;
        this.size = size;
        this.description = description;
    }
}