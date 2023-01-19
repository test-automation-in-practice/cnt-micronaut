package example.micronaut.graphql.datafetcher

import com.fasterxml.jackson.databind.ObjectMapper
import example.micronaut.graphql.IdGenerator
import example.micronaut.graphql.books.core.Book
import example.micronaut.graphql.books.core.BookRecord
import example.micronaut.graphql.books.core.BookRepository
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import jakarta.inject.Singleton

@Singleton
class AddBookDataFetcher(
    private val repository: BookRepository,
    private val objectMapper: ObjectMapper,
    private val idGenerator: IdGenerator
) : DataFetcher<BookRecord> {

    private data class BookInfo(val title: String, val isbn: String)

    override fun get(environment: DataFetchingEnvironment): BookRecord {
        val (title, isbn) = objectMapper.convertValue(environment.getArgument("bookInfo"), BookInfo::class.java)

        return repository.save(BookRecord(idGenerator.generateId(), Book(isbn, title)))
    }
}
