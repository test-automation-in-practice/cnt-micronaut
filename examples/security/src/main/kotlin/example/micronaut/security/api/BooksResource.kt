package example.micronaut.security.api

import example.micronaut.security.core.BookCollection
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.validation.Valid


@Controller("/books")
open class BooksResource(private val bookCollection: BookCollection) {

    @Get
    @Secured(SecurityRule.IS_AUTHENTICATED)
    open fun findBooks(): Collection<BookRepresentation> {
        return findBooksInternal()
    }

    @Post
    @Secured("BOOK_CREATOR")
    open fun createBook(@Valid @Body createBookRequest: CreateBookRequest): HttpResponse<BookRepresentation> {
        return createBookInternal(createBookRequest)
    }

    // the following two endpoint are secured via configuration of intercept URL map. See
    // application.yaml for details.
    @Get("/url-not-secured")
    open fun findBooksUnsecured(): Collection<BookRepresentation> {
        return findBooksInternal()
    }

    @Post("/url-not-secured")
    open fun createBookUnsecured(@Valid @Body createBookRequest: CreateBookRequest): HttpResponse<BookRepresentation> {
        return createBookInternal(createBookRequest)
    }

    private fun findBooksInternal(): List<BookRepresentation> {
        return bookCollection.getAll().map { BookRepresentation(it) }
    }

    private fun createBookInternal(createBookRequest: CreateBookRequest): HttpResponse<BookRepresentation> {
        val book = bookCollection.add(createBookRequest.isbn, createBookRequest.title)
        return HttpResponse.created(BookRepresentation(book))
    }
}
