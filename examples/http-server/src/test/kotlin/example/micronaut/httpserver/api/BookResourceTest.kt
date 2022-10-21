package example.micronaut.httpserver.api

import example.micronaut.httpserver.Examples
import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class BookResourceTest {

    @Inject
    private lateinit var bookCollection: BookCollection

    @Nested
    @DisplayName("GET /default-api/books")
    inner class GET {
        @Test
        fun `returns 200 for existing Book`(spec: RequestSpecification) {
            val bookRecord = bookCollection.add(Examples.book_cleanCode)

            spec
                .get("/books/${bookRecord.id}")
                .then()
                .statusCode(200)
                .body(
                    "id", equalTo(bookRecord.id.toString()),
                    "title", equalTo(bookRecord.book.title),
                    "isbn", equalTo(bookRecord.book.isbn)
                )
        }

        @Test
        fun `returns 404 for non-existing Book`(spec: RequestSpecification) {
            spec
                .get("/books/${UUID.randomUUID()}")
                .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("POST /de")
    inner class POST {

    }
}
