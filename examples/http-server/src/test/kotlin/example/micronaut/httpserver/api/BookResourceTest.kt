package example.micronaut.httpserver.api

import example.micronaut.httpserver.Examples
import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class BookResourceTest (private val spec: RequestSpecification) {

    @Inject
    private lateinit var bookCollection: BookCollection

    @Nested
    @DisplayName("GET /default-api/books")
    inner class GET {
        @Test
        fun `returns 200 for existing Book`() {
            val bookRecord = bookCollection.add(Examples.book_cleanCode)

            spec
                .get("/default-api/books/${bookRecord.id}")
                .then()
                .statusCode(200)
                .body(
                    "id", equalTo(bookRecord.id.toString()),
                    "title", equalTo(bookRecord.book.title),
                    "isbn", equalTo(bookRecord.book.isbn)
                )
        }

        @Test
        fun `returns 404 for non-existing Book`() {
            spec
                .get("/default-api/books/${UUID.randomUUID()}")
                .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("POST /default-api/books")
    inner class POST {
        @Test
        fun `adds a book to the library and returns resource representation`() {
            spec
                .contentType(ContentType.JSON)
                .body("""{ "title": "Clean Code", "isbn": "9780132350884" }""")
                .post("/default-api/books")
                .then()
                .statusCode(201)
                .body(
                    "id", CoreMatchers.notNullValue(),
                    "title", equalTo("Clean Code"),
                    "isbn", equalTo("9780132350884")
                )
        }

        @Test
        fun `responds with status 400 if a request property is malformed`() {
            spec
                .contentType(ContentType.JSON)
                .body("""{ "title": "Clean Code", "isbn": "0132350884" }""") // isbn malformed
                .post("/default-api/books")
                .then()
                .statusCode(400)
        }

        @Test
        fun `responds with status 400 if a required request property is missing`() {
            spec
                .contentType(ContentType.JSON)
                .body("""{ "isbn": "0132350884" }""") // title missing
                .post("/default-api/books")
                .then()
                .statusCode(400)
        }
    }
}
