package org.brain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.brain.loader.JsonDataLoader;
import org.brain.model.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {
    private final List<File> files = new ArrayList<>();

    public FileService(String filePath) {
        try {
            // Load files from JSON file
            files.addAll(JsonDataLoader.loadData(filePath, new TypeReference<List<File>>() {}));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load files from file", e);
        }
    }

    public List<File> getFilesByFolderId(String folderId) {
        return files.stream()
                .filter(file -> file.getFolderId().equals(folderId))
                .collect(Collectors.toList());
    }

    public File createFile(String folderId, String name, int size, String description) {
        File file = new File(folderId, name, size, description);
        files.add(file);
        return file;
    }
}