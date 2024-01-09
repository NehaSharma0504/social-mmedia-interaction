package eca.learnings.socialmedia.interaction.config;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;

import java.io.InputStreamReader;

public class SocialMediaConfig {
    @Bean
    public GraphQL graphQL(GraphQLDataFetchers graphQLDataFetchers) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new InputStreamReader(getClass().getResourceAsStream("/schema.graphqls")));
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("getUserNotifications", graphQLDataFetchers.getUserNotificationsDataFetcher()))
                .build();

        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
