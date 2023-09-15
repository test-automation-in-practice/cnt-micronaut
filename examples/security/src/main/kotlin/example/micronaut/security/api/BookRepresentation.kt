package example.micronaut.security.api

import example.micronaut.security.core.Book
import java.util.UUID

data class BookRepresentation(
    val id: UUID,
    val title: String,
    val isbn: String
) {
    constructor(book: Book) : this(book.id, book.title, book.isbn)
}
