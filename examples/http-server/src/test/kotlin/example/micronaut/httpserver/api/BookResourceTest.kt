package example.micronaut.httpserver.api

import example.micronaut.httpserver.Examples.bookRecord_cleanArchitecture
import example.micronaut.httpserver.Examples.bookRecord_cleanCode
import example.micronaut.httpserver.Examples.book_cleanArchitecture
import example.micronaut.httpserver.Examples.book_cleanCode
import example.micronaut.httpserver.books.core.BookCollection
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponseOptions
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*

@MicronautTest
internal class BookResourceTest(private val spec: RequestSpecification) {

    @Inject
    private lateinit var bookCollection: BookCollection

    @MockBean(BookCollection::class)
    fun mockBookCollection(): BookCollection {
        return mockk()
    }

    @Nested
    @DisplayName("GET /default-api/books")
    inner class Get {
        @Test
        fun `returns all books in the collection`() {
            every { bookCollection.getAll() } returns listOf(bookRecord_cleanCode, bookRecord_cleanArchitecture)

            spec.get("/default-api/books")
                .then()
                .statusCode(200)
                .assertBodyEqualsJson(
                    """
                        [
                            {
                                "id": "${bookRecord_cleanCode.id}",
                                "isbn": "${book_cleanCode.isbn}",
                                "title": "${book_cleanCode.title}"
                            },
                            {
                                "id": "${bookRecord_cleanArchitecture.id}",
                                "isbn": "${book_cleanArchitecture.isbn}",
                                "title": "${book_cleanArchitecture.title}"
                            }
                        ]
                    """
                )
        }
    }

    @Nested
    @DisplayName("GET /default-api/books/{id}")
    inner class GetById {
        @Test
        fun `returns 200 for existing Book`() {
            every { bookCollection.get(bookRecord_cleanCode.id) } returns bookRecord_cleanCode

            spec
                .get("/default-api/books/${bookRecord_cleanCode.id}")
                .then()
                .statusCode(200)
                .assertBodyEqualsJson(
                    """{
                        "id": "${bookRecord_cleanCode.id}",
                        "isbn": "${book_cleanCode.isbn}",
                        "title": "${book_cleanCode.title}"
                    }"""
                )
        }

        @Test
        fun `returns 404 for non-existing Book`() {
            val bookId = UUID.randomUUID()
            every { bookCollection.get(bookId) } returns null
            spec
                .get("/default-api/books/$bookId")
                .then()
                .statusCode(404)
        }
    }

    @Nested
    @DisplayName("POST /default-api/books")
    inner class Post {
        @Test
        fun `adds a book to the library and returns resource representation`() {
            every { bookCollection.add(any()) } returns bookRecord_cleanCode

            spec
                .contentType(ContentType.JSON)
                .body("""{ "title": "Clean Code", "isbn": "9780132350884" }""")
                .post("/default-api/books")
                .then()
                .statusCode(201)
                .assertBodyEqualsJson(
                    """
                    {
                        "id": "${bookRecord_cleanCode.id.toString()}",
                        "title": "${book_cleanCode.title}",
                        "isbn": "${book_cleanCode.isbn}"
                    }
                    """
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

    private fun ValidatableResponseOptions<*, *>.assertBodyEqualsJson(expectedJson: String) {
        JSONAssert.assertEquals(
            expectedJson,
            extract().body().asString(),
            true
        )
    }
}
