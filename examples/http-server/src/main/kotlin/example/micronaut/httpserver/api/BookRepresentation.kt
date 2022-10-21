package example.micronaut.httpserver.api

import example.micronaut.httpserver.books.core.BookRecord
import java.util.UUID

data class BookRepresentation(
    val id: UUID,
    val title: String,
    val isbn: String
)

fun BookRecord.toRepresentation() =
    BookRepresentation(
        id = id,
        title = book.title,
        isbn = book.isbn
    )
