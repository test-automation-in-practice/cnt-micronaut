package example.micronaut.httpserver

import example.micronaut.httpserver.books.core.Book
import example.micronaut.httpserver.books.core.BookRecord
import java.time.Instant
import java.util.UUID

object Examples {

    val book_cleanCode = Book(
        isbn = "9780132350884",
        title = "Clean Code"
    )

    val bookRecord_cleanCode = BookRecord(UUID.randomUUID(), book_cleanCode)

    val book_cleanArchitecture = Book (
        isbn = " 9780134494166",
        title = "Clean Architecture"
    )

    val bookRecord_cleanArchitecture = BookRecord(UUID.randomUUID(), book_cleanArchitecture)
}
