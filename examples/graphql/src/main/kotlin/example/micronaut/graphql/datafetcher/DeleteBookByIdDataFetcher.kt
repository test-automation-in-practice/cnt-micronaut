package example.micronaut.graphql.datafetcher

import example.micronaut.graphql.books.core.BookRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton
import java.util.*

@Singleton
class DeleteBookByIdDataFetcher(private val repository: BookRepository) : DataFetcher<Boolean> {
    override fun get(environment: DataFetchingEnvironment): Boolean {
        val id = environment.getArgument<String>("id")
        return repository.deleteById(UUID.fromString(id))
    }
}