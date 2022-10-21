package example.micronaut.httpserver.port

import example.micronaut.httpserver.Examples
import example.micronaut.httpserver.books.core.BookCollection
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest
internal class BookResourceTest {

    @Inject
    private lateinit var bookCollection: BookCollection

    @Test
    fun `finding a Book by its ID returns 200 with found Book`(spec: RequestSpecification) {
        val bookRecord = bookCollection.add(Examples.book_cleanCode)
        val bookRepresentation = spec.`when`().get("/books/${bookRecord.id}")
            .then()
            .statusCode(200)
            .extract().`as`(BookRepresentation::class.java)

        assertSoftly(bookRepresentation) {
            with(it) {
                id shouldBe bookRecord.id
                title shouldBe bookRecord.book.title
                isbn shouldBe bookRecord.book.isbn
            }
        }
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