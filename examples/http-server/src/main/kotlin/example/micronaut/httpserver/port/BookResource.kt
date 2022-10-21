package example.micronaut.httpserver.port

import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*


@Controller("/books")
class BookResource(private val bookCollection: BookCollection) {

    @Get("/{bookId}")
    fun findBookById(@PathVariable("bookId") bookId: String): HttpResponse<*> {
        val bookRecord = bookCollection.get(UUID.fromString(bookId))

        return if (bookRecord != null) {
            HttpResponse.ok(bookRecord.toRepresentation())
        } else {
            HttpResponse.notFound<Void>()
        }
    }
}
