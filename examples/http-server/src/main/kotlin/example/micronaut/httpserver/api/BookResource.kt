package example.micronaut.httpserver.api

import example.micronaut.httpserver.books.core.Book
import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import jakarta.validation.Valid
import java.util.UUID


@Controller("/default-api/books")
open class BookResource(private val bookCollection: BookCollection) {

    @Get
    open fun findBooks() : Collection<BookRepresentation> {
        return bookCollection.getAll().map { BookRepresentation(it) }
    }

    @Post
    @Status(HttpStatus.CREATED)
    open fun post(@Valid @Body createBookRequest: CreateBookRequest): HttpResponse<BookRepresentation> {
        val bookRecord = bookCollection.add(Book(createBookRequest.isbn, createBookRequest.title))
        return HttpResponse.created(BookRepresentation(bookRecord))
    }

    @Get("/{bookId}")
    open fun findBookById(@PathVariable("bookId") bookId: String): HttpResponse<*> {
        val bookRecord = bookCollection.get(UUID.fromString(bookId))

        return if (bookRecord != null) {
            HttpResponse.ok(BookRepresentation(bookRecord))
        } else {
            HttpResponse.notFound<Void>()
        }
    }
}
