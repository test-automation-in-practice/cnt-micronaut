package example.micronaut.graphql

import example.micronaut.graphql.Examples.record_apiDesign_enriched
import example.micronaut.graphql.Examples.record_cleanCode_enriched
import example.micronaut.graphql.books.core.BookRepository
import io.micronaut.http.HttpHeaders
import io.micronaut.http.MediaType
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.restassured.response.ValidatableResponseOptions
import io.restassured.specification.RequestSpecification
import jakarta.inject.Inject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*

@MicronautTest
class GraphqlControllerTest(
    private val spec: RequestSpecification,
    private val repository: BookRepository,
    private val idGenerator: IdGenerator
) {

    @MockBean(IdGenerator::class)
    fun mockIdGenerator() = mockk<IdGenerator>()

    @Nested
    inner class Query {

        @Nested
        inner class All {

            @Test
            fun `returns all stored BookRecords`() {
                val cleanCode = repository.save(record_cleanCode_enriched)
                val apiDesign = repository.save(record_apiDesign_enriched)

                queryAndExpect(
                    query = """{ getAllBooks { id, timestamp, book { isbn, numberOfPages, authors} } }""",
                    response =
                    """
                        {
                            "data": {
                                "getAllBooks": [
                                    {
                                        "id": "${cleanCode.id}",
                                        "timestamp": "${cleanCode.timestamp}",
                                        "book": {
                                            "isbn": "${cleanCode.book.isbn}",
                                            "numberOfPages": ${cleanCode.book.numberOfPages},
                                            "authors": [
                                                ${cleanCode.book.authors.joinToString { "\"$it\"" }}
                                            ]
                                        }
                                    },
                                    {
                                        "id": "${apiDesign.id}",
                                        "timestamp": "${apiDesign.timestamp}",
                                        "book": {
                                            "isbn": "${apiDesign.book.isbn}",
                                            "numberOfPages": ${apiDesign.book.numberOfPages},
                                            "authors": [
                                                ${apiDesign.book.authors.joinToString { "\"$it\"" }}
                                            ]
                                        }
                                    }
                                ]
                            }
                        }
                    }"""
                )
            }

            @Test
            fun `returns empty array if none exist`() {
                queryAndExpect(
                    query = """{ getAllBooks { id, timestamp, book { isbn, numberOfPages, authors} } }""",
                    response =
                    """
                        {
                            "data": {
                                "getAllBooks": []
                            }
                        }
                    }"""
                )
            }
        }

        @Nested
        inner class ById {

            @Test
            fun `returns body for existing BookRecord`() {
                val savedBookRecord = repository.save(record_cleanCode_enriched)

                queryAndExpect(
                    query = """{ bookById(id: "${savedBookRecord.id}") { id, timestamp, book { isbn, numberOfPages, authors} } }""",
                    response =
                    """
                        {
                            "data": {
                                "bookById": {
                                    "book": {
                                        "isbn": "${savedBookRecord.book.isbn}",
                                        "numberOfPages": ${savedBookRecord.book.numberOfPages},
                                        "authors": [
                                            ${savedBookRecord.book.authors.joinToString { "\"$it\"" }}
                                        ]
                                    },
                                    "id": "${savedBookRecord.id}",
                                    "timestamp": "${savedBookRecord.timestamp}"
                                }
                            }
                        }
                    """
                )
            }

            @Test
            fun `returns error in body for not existing BookRecord`() {
                val idForNotExistingBook = UUID.randomUUID()
                val query =
                    """{ bookById(id: "$idForNotExistingBook") { id, timestamp, book { isbn, numberOfPages, authors} } }"""

                queryAndExpect(
                    strict = false,
                    query = query,
                    response =
                    """
                        {
                          "errors": [
                            {
                              "message": "Exception while fetching data (/bookById) : Book not found with ID ${idForNotExistingBook}.",
                              "path": [
                                "bookById"
                              ],
                              "extensions": {
                                "classification": "DataFetchingException"
                              }
                            }
                          ]
                        }
                    """
                )
            }
        }

        private fun queryAndExpect(query: String, response: String, strict: Boolean = true) {
            spec
                .log()
                .body()
                .queryParam("query", query)
                .get("/graphql")
                .then()
                .log()
                .body()
                .assertBodyEqualsJson(response, strict)
        }
    }

    @Nested
    inner class Mutation {

        @Nested
        inner class Adding {

            @Test
            fun `returns added BookRecord`() {
                val id = UUID.randomUUID()
                every { idGenerator.generateId() } returns id
                val mutation = """
                           mutation {
                                addBook(bookInfo: { title: "Project Hail Mary", isbn: "98123812309478" }) {
                                    id
                                    book {
                                        isbn
                                        title
                                    }
                                }
                            }
                    """
                val query = GraphQLQuery(mutation)
                mutateAndExpect(
                    mutation = query,
                    response = """
                       {
                            "data": {
                                "addBook": {
                                    "id": "$id",
                                    "book": {
                                        "isbn": "98123812309478",
                                        "title": "Project Hail Mary"
                                    }
                                }
                            }
                       }
                    """
                )
            }
        }

        @Nested
        inner class Deleting {

            @Test
            fun `returns true on existing BookRecord`() {
                repository.save(record_cleanCode_enriched)
                val mutation = """
                        mutation { 
                            deleteBookById(id: "${record_cleanCode_enriched.id}") 
                        }"""
                val query = GraphQLQuery(mutation)
                mutateAndExpect(
                    mutation = query,
                    response = """ { "data": { "deleteBookById": true } } """
                )
            }

            @Test
            fun `returns false on not existing BookRecord`() {
                val mutation = """
                        mutation { 
                            deleteBookById(id: "${UUID.randomUUID()}") 
                        }"""
                val query = GraphQLQuery(mutation)
                mutateAndExpect(
                    mutation = query,
                    response = """ { "data": { "deleteBookById": false } } """
                )
            }
        }

        // We let RestAssured serialize the payload for us using this object. That's just for convenience.
        private inner class GraphQLQuery(val query: String)

        private fun mutateAndExpect(mutation: Any, response: String, strict: Boolean = true) {
            spec
                .log()
                .body()
                .body(mutation)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post("/graphql")
                .then()
                .log()
                .body().assertBodyEqualsJson(response, strict)
        }
    }

    private fun ValidatableResponseOptions<*, *>.assertBodyEqualsJson(expectedJson: String, strict: Boolean = true) {
        JSONAssert.assertEquals(expectedJson, extract().body().asString(), strict)
    }
}
