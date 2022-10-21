package example.micronaut.httpserver.port

import example.micronaut.httpserver.Examples
import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class BookResourceTest {

    @Inject
    private lateinit var bookCollection: BookCollection

    @Test
    fun `finding a Book by its ID returns 200 with found Book`(spec: RequestSpecification) {
        val bookRecord = bookCollection.add(Examples.book_cleanCode)
        spec.get("/books/${bookRecord.id}")
            .then()
            .statusCode(200)
            .body(
                "id", equalTo(bookRecord.id.toString()),
                "title", equalTo(bookRecord.book.title),
                "isbn", equalTo(bookRecord.book.isbn)
            )
    }

    @Test
    fun `finding a Book by its ID returns 404 for non-existing Book`(spec: RequestSpecification) {
        spec
            .`when`()
            .get("/books/${UUID.randomUUID()}")
            .then()
            .statusCode(404)
    }
}
