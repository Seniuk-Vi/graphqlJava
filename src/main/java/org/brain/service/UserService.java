package org.brain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.brain.loader.JsonDataLoader;
import org.brain.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final List<User> users = new ArrayList<>();

    public UserService(String filePath) {
        try {
            // Load users from JSON file
            users.addAll(JsonDataLoader.loadData(filePath, new TypeReference<List<User>>() {}));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from file", e);
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(String name) {
        User user = new User(name);
        users.add(user);
        return user;
    }
}