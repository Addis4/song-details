package com.knoldus.controller;

import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.knoldus.entity.GraphQLDataFetchers;
import graphql.schema.StaticDataFetcher;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@GraphQLTest
public class GraphQLProviderTest {

    GraphQLProvider mockGraphQLProvider = mock(GraphQLProvider.class);
    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;
    @Mock
    private GraphQLDataFetchers graphQLDataFetchers;

    @Test
    void init() throws IOException {
        when(graphQLDataFetchers.getSongByIdDataFetcher()).thenReturn(new StaticDataFetcher("name"));
        when(graphQLDataFetchers.getArtistByIdDataFetcher()).thenReturn(new StaticDataFetcher("firstName"));
        doNothing().when(mockGraphQLProvider).init();
        mockGraphQLProvider.init();
        verify(mockGraphQLProvider, times(1)).init();
    }

}