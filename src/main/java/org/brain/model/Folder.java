package org.brain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Folder {
    private String id;
    private String userId;
    private String name;

    public Folder(String userId, String name) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.name = name;
    }
}