package example.micronaut.graphql

import example.micronaut.graphql.books.core.Book
import example.micronaut.graphql.books.core.BookRecord
import java.time.Instant
import java.util.UUID

object Examples {

    private val book_cleanCode_enriched = Book(
        isbn = "9780132350884",
        title = "Clean Code",
        description = "Lorem Ipsum ...",
        authors = listOf("Robert C. Martin", "Dean Wampler"),
        numberOfPages = 464
    )

    private val book_apiDesign_enriched = Book(
        isbn = "1239710283041",
        title = "API Design",
        description = "Lorem Ipsum ...",
        authors = listOf("Somebody Pretty Famous"),
        numberOfPages = 333
    )

    val record_cleanCode_enriched = BookRecord(
        id = UUID.fromString("b3fc0be8-463e-4875-9629-67921a1e00f4"),
        book = book_cleanCode_enriched,
        timestamp = Instant.parse("2021-06-25T12:34:56.789Z")
    )

    val record_apiDesign_enriched = BookRecord(
        id = UUID.fromString("29bb58bc-c7bc-4d2e-bc14-e80a9271d4c0"),
        book = book_apiDesign_enriched,
        timestamp = Instant.parse("2023-01-01T12:34:56.789Z")
    )
}
