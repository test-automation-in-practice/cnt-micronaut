package example.micronaut.graphql.datafetcher

import example.micronaut.graphql.books.core.BookRecord
import example.micronaut.graphql.books.core.BookRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class GetBookRecordByIdDataFetcher(private val repository: BookRepository) : DataFetcher<BookRecord> {

    override fun get(environment: DataFetchingEnvironment): BookRecord {
        val id = environment.getArgument<String>("id")
        return repository.findById(UUID.fromString(id)) ?: throw IllegalArgumentException("Book not found with ID ${id}.")
    }
}