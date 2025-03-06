package org.brain;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.brain.fetcher.DataFetchers;
import org.brain.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GraphQLApp {
    private final GraphQL graphQL;

    public GraphQLApp(DataFetchers dataFetchers) {
        // Завантаження схеми
        String schemaContent = readSchema();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaContent);

        // Налаштування RuntimeWiring
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("users", dataFetchers.getAllUsersFetcher())
                        .dataFetcher("userById", dataFetchers.getUserByIdFetcher()))
                .type("User", builder -> builder
                        .dataFetcher("folders", dataFetchers.getUserDataFetcher()))
                .type("Folder", builder -> builder
                        .dataFetcher("files", dataFetchers.getFolderFilesFetcher())
                        .dataFetcher("id", graphql.schema.PropertyDataFetcher.fetching("id")))
                .type("Mutation", builder -> builder
                        .dataFetcher("createUser", dataFetchers.createUserFetcher())
                        .dataFetcher("createFolder", dataFetchers.createFolderFetcher())
                        .dataFetcher("createFile", dataFetchers.createFileFetcher()))
                .build();

        // Створення GraphQLSchema
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

        // Створення GraphQL
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    /**
     * Виконує GraphQL-запит і повертає результат.
     *
     * @param query Запит у форматі GraphQL
     * @return Результат виконання запиту
     */
    public ExecutionResult executeQuery(String query) {
        return graphQL.execute(query);
    }

    private String readSchema() {
        try {
            return Files.readString(new File("src/main/resources/schema.graphqls").toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read schema file", e);
        }
    }
}
