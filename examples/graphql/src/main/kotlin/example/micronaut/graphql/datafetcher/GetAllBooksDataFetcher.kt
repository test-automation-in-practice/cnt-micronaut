package example.micronaut.graphql.datafetcher

import example.micronaut.graphql.books.core.BookRecord
import example.micronaut.graphql.books.core.BookRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton

@Singleton
class GetAllBooksDataFetcher(private val repository: BookRepository) : DataFetcher<Collection<BookRecord>> {

    override fun get(environment: DataFetchingEnvironment): Collection<BookRecord> {
        return repository.getAll()
    }
}