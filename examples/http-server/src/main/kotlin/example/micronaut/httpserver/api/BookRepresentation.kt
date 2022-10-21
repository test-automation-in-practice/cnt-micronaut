package example.micronaut.httpserver.api

import example.micronaut.httpserver.books.core.BookRecord
import java.util.UUID

data class BookRepresentation(
    val id: UUID,
    val title: String,
    val isbn: String
) {
    constructor(bookRecord: BookRecord) : this (bookRecord.id, bookRecord.book.title, bookRecord.book.isbn)
}
