package example.micronaut.graphql

import example.micronaut.graphql.datafetcher.AddBookDataFetcher
import example.micronaut.graphql.datafetcher.DeleteBookByIdDataFetcher
import example.micronaut.graphql.datafetcher.GetAllBooksDataFetcher
import example.micronaut.graphql.datafetcher.GetBookRecordByIdDataFetcher
import graphql.GraphQL
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory.getLogger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Instant
import java.util.Locale

@Factory
class GraphQLConfiguration {

    private val logger = getLogger(javaClass)

    @Bean
    @Singleton
    fun graphQL(
        resourceResolver: ResourceResolver,
        getBookRecordByIdDataFetcher: GetBookRecordByIdDataFetcher,
        getAllBooksDataFetcher: GetAllBooksDataFetcher,
        addBookDataFetcher: AddBookDataFetcher,
        deleteBookByIdDataFetcher: DeleteBookByIdDataFetcher
    ): GraphQL {
        val schemaParser = SchemaParser()

        val typeRegistry = TypeDefinitionRegistry()
        val graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls")

        return graphqlSchema.map {
            typeRegistry.merge(schemaParser.parse(BufferedReader(InputStreamReader(it))))
            val runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .scalar(instantScalar)
                .type("Query") { typeWiring ->
                    typeWiring
                        .dataFetcher("bookById", getBookRecordByIdDataFetcher)
                        .dataFetcher("getAllBooks", getAllBooksDataFetcher)
                }
                .type("Mutation") { typeWiring ->
                    typeWiring
                        .dataFetcher("addBook", addBookDataFetcher)
                        .dataFetcher("deleteBookById", deleteBookByIdDataFetcher)
                }
                .build()

            val schemaGenerator = SchemaGenerator()
            val graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
            GraphQL.newGraphQL(graphQLSchema).build()
        }.orElseGet {
            logger.debug("No GraphQL services found, returning empty schema")
            GraphQL.Builder(GraphQLSchema.newSchema().build()).build()
        }
    }

    private val instantScalar: GraphQLScalarType =
        GraphQLScalarType.newScalar().name("Instant").description("A custom scalar that handles [java.time.Instant]s.")
            .coercing(object : Coercing<Instant, String> {

                override fun serialize(
                    dataFetcherResult: Any,
                    graphQLContext: GraphQLContext,
                    locale: Locale
                ): String {
                    return dataFetcherResult.toString()
                }

                override fun parseValue(
                    input: Any,
                    graphQLContext: GraphQLContext,
                    locale: Locale
                ): Instant {
                    return Instant.parse(input.toString())
                }

                override fun parseLiteral(
                    input: Value<*>,
                    variables: CoercedVariables,
                    graphQLContext: GraphQLContext,
                    locale: Locale
                ): Instant {
                    return Instant.parse(input.toString())
                }
            })
            .build()
}
