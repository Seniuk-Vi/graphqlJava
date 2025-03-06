package org.brain;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.brain.fetcher.DataFetchers;
import org.brain.service.FileService;
import org.brain.service.FolderService;
import org.brain.service.UserService;

public class Main {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            UserService userService = new UserService("src/main/resources/users.json");
            FolderService folderService = new FolderService("src/main/resources/folders.json");
            FileService fileService = new FileService("src/main/resources/files.json");

            DataFetchers dataFetchers = new DataFetchers(userService, folderService, fileService);

            GraphQLApp graphQLApp = new GraphQLApp(dataFetchers);

            String[] scripts = {
                    // 1. Get All Users
                    """
                query {
                    users {
                        id
                        name
                        folders {
                            id
                            name
                            files {
                                id
                                name
                                size
                                description
                            }
                        }
                    }
                }
                """,
                    // 2. Get a User by ID
                    """
                query {
                    userById(id: "1") {
                        id
                        name
                        folders {
                            id
                            name
                            files {
                                id
                                name
                                size
                                description
                            }
                        }
                    }
                }
                """,
                    // 3. Get a User Without Folders
                    """
                query {
                    userById(id: "1") {
                        id
                        name
                    }
                }
                """,
                    // 4. Create a New User
                    """
                mutation {
                    createUser(input: { name: "Charlie" }) {
                        id
                        name
                    }
                }
                """,
                    // 5. Create a Folder for a User
                    """
                mutation {
                    createFolder(userId: "1", folderName: "Projects") {
                        id
                        name
                    }
                }
                """,
                    // 6. Create a File in a Folder
                    """
                mutation {
                    createFile(folderId: "101", input: { name: "project_plan.pdf", size: 2048, description: "Project plan document" }) {
                        id
                        name
                        size
                        description
                    }
                }
                """,
                    // 7. Retrieve Folders for a User
                    """
                query {
                    userById(id: "1") {
                        folders {
                            id
                            name
                        }
                    }
                }
                """,
                    // 8. Retrieve Files for a Folder
                    """
                query {
                    userById(id: "1") {
                        folders {
                            id
                            name
                            files {
                                id
                                name
                                size
                                description
                            }
                        }
                    }
                }
                """,
                    // 9. Create a File Without Description
                    """
                mutation {
                    createFile(folderId: "102", input: { name: "notes.txt", size: 512 }) {
                        id
                        name
                        size
                        description
                    }
                }
                """,
                    // 10. Get All Folders and Files for a Specific User
                    """
                query {
                    userById(id: "2") {
                        id
                        name
                        folders {
                            id
                            name
                            files {
                                id
                                name
                                size
                                description
                            }
                        }
                    }
                }
                """
            };

            for (int i = 0; i < scripts.length; i++) {
                System.out.println("=== Script " + (i + 1) + " ===");
                executeAndPrint(graphQLApp, scripts[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeAndPrint(GraphQLApp graphQLApp, String query) {
        ExecutionResult result = graphQLApp.executeQuery(query);

        try {

            String formattedSpecification = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result.toSpecification());
            System.out.println(formattedSpecification);

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}