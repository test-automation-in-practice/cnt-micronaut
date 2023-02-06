package example.micronaut.rabbitmq.business

import example.spring.boot.rabbitmq.business.Book
import example.spring.boot.rabbitmq.business.BookRecord
import java.util.*

object Examples {

    val cleanCode = BookRecord(
        id = UUID.fromString("b3fc0be8-463e-4875-9629-67921a1e00f4"),
        book = Book(
            isbn = "9780132350884",
            title = "Clean Code"
        )
    )

}
