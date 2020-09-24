package com.knoldus.entity;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GraphQLDataFetchersTest {

    private final String schema = "type Query {\n" +
            "    songById(id: ID): Song\n" +
            "}\n" +
            "\n" +
            "type Song {\n" +
            "    id: ID\n" +
            "    name: String\n" +
            "    genre: String\n" +
            "    artist: Artist\n" +
            "}\n" +
            "\n" +
            "type Artist {\n" +
            "    id: ID\n" +
            "    firstName: String\n" +
            "    lastName: String\n" +
            "}";
    @Autowired
    private GraphQLDataFetchers graphQLDataFetchers;
    private SchemaParser schemaParser = new SchemaParser();
    private TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    @Test
    void getSongDetailsById() {
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetcher("songById", graphQLDataFetchers.getSongByIdDataFetcher()))
                .build();
        GraphQL graphQL = convertToExecutableGraphQL(runtimeWiring);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { songById(id: \"song-1\") { name } }")
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        assertEquals(executionResult.getData().toString(), "{songById={name=Shape of you}}");
    }

    @Test
    void notGetSongDetailsByWrongId() {
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetcher("songById", graphQLDataFetchers.getSongByIdDataFetcher()))
                .build();
        GraphQL graphQL = convertToExecutableGraphQL(runtimeWiring);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { songById(id: \"song-4\") { name } }")
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        assertEquals(executionResult.getData().toString(), "{songById=null}");
    }

    @Test
    void getArtistById() {
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetcher("songById", graphQLDataFetchers.getSongByIdDataFetcher()))
                .type(newTypeWiring("Song").dataFetcher("artist", graphQLDataFetchers.getArtistByIdDataFetcher()))
                .build();
        GraphQL graphQL = convertToExecutableGraphQL(runtimeWiring);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { songById(id: \"song-1\") { artist{ firstName }} }")
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        assertEquals(executionResult.getData().toString(), "{songById={artist={firstName=Ed}}}");
    }

    @Test
    void getWithoutUsingSongByID() {
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type(newTypeWiring("Song").dataFetcher("artist", graphQLDataFetchers.getArtistByIdDataFetcher()))
                .build();
        GraphQL graphQL = convertToExecutableGraphQL(runtimeWiring);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { songById(id: \"song-1\") { artist{ firstName }} }")
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        assertEquals(executionResult.getData().toString(), "{songById=null}");
    }

    @Test
    void getNoArtistByWrongId() {
        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetcher("songById", graphQLDataFetchers.getSongByIdDataFetcher()))
                .type(newTypeWiring("Song").dataFetcher("artist", graphQLDataFetchers.getArtistByIdDataFetcher()))
                .build();
        GraphQL graphQL = convertToExecutableGraphQL(runtimeWiring);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { songById(id: \"song-4\") { artist{ firstName }} }")
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        assertEquals(executionResult.getData().toString(), "{songById=null}");
    }

    private GraphQL convertToExecutableGraphQL(RuntimeWiring runtimeWiring) {
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

}