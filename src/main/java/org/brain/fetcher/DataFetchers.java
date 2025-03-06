package org.brain.fetcher;
import graphql.schema.DataFetcher;
import org.brain.model.File;
import org.brain.model.Folder;
import org.brain.model.User;
import org.brain.payload.CreateFileInput;
import org.brain.payload.CreateUserInput;
import org.brain.service.FileService;
import org.brain.service.FolderService;
import org.brain.service.UserService;

import java.util.List;
import java.util.Map;

public class DataFetchers {
    private final UserService userService;
    private final FolderService folderService;
    private final FileService fileService;

    public DataFetchers(UserService userService, FolderService folderService, FileService fileService) {
        this.userService = userService;
        this.folderService = folderService;
        this.fileService = fileService;
    }

    // Query: Get all users
    public DataFetcher<List<User>> getAllUsersFetcher() {
        return environment -> {
            System.out.println("DataFetchers.getAllUsersFetcher");
            return userService.getAllUsers();

        };
    }

    // Query: Get user by ID
    public DataFetcher<User> getUserByIdFetcher() {
        return environment -> {
            System.out.println("DataFetchers.getUserByIdFetcher");
            String id = environment.getArgument("id");
            return userService.getUserById(id).orElse(null);
        };
    }

    // Query: Get folders (UserData) for a user
    public DataFetcher<List<Folder>> getUserDataFetcher() {
        return environment -> {
            System.out.println("DataFetchers.getUserDataFetcher");
            User user = environment.getSource();
            if (user != null) {
                return folderService.getFoldersByUserId(user.getId());
            }
            return null;
        };
    }

    // Query: Get files for a folder
    public DataFetcher<List<File>> getFolderFilesFetcher() {
        return environment -> {
            Folder folder = environment.getSource();
            if (folder != null) {
                System.out.println("DataFetchers.getFolderFilesFetcher: " + folder.getId());
                return fileService.getFilesByFolderId(folder.getId());
            }
            return null;
        };
    }

    // Mutation: Create a new user
    public DataFetcher<User> createUserFetcher() {
        return environment -> {
            System.out.println("DataFetchers.createUserFetcher");

            CreateUserInput input = environment.getArgument("input");

            String name = input.getName();

            return userService.createUser(name);
        };
    }

    // Mutation: Create a new folder for a user
    public DataFetcher<Folder> createFolderFetcher() {
        return environment -> {
            System.out.println("DataFetchers.createFolderFetcher");
            String userId = environment.getArgument("userId");
            String folderName = environment.getArgument("folderName");
            return folderService.createFolder(userId, folderName);
        };
    }

    // Mutation: Create a new file in a folder
    public DataFetcher<File> createFileFetcher() {
        return environment -> {
            System.out.println("DataFetchers.createFileFetcher");

            // Get the folderId argument
            String folderId = environment.getArgument("folderId");

            CreateFileInput input = environment.getArgument("input");

            String name = input.getName();
            int size = input.getSize();
            String description = input.getDescription();

            return fileService.createFile(folderId, name, size, description);
        };
    }
}