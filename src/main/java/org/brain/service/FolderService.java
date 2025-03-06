package org.brain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.brain.loader.JsonDataLoader;
import org.brain.model.Folder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FolderService {
    private final List<Folder> folders = new ArrayList<>();

    public FolderService(String filePath) {
        try {
            // Load folders from JSON file
            folders.addAll(JsonDataLoader.loadData(filePath, new TypeReference<List<Folder>>() {}));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load folders from file", e);
        }
    }

    public List<Folder> getFoldersByUserId(String userId) {
        return folders.stream()
                .filter(folder -> folder.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Folder> getFolderById(String folderId) {
        return folders.stream()
                .filter(folder -> folder.getId().equals(folderId))
                .findFirst();
    }

    public Folder createFolder(String userId, String name) {
        Folder folder = new Folder(userId, name);
        folders.add(folder);
        return folder;
    }
}