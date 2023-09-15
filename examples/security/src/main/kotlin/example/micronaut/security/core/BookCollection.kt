package example.micronaut.security.core

import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class BookCollection {

    private val books = mutableListOf<Book>()

    fun getAll(): Collection<Book> = books

    fun add(isbn: String, title: String): Book {
        val book = Book(UUID.randomUUID(), isbn, title)
        books.add(book)
        return book
    }
}
