package example.micronaut.security.api

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest
class BooksResourceBasicAuthTest(
    private val spec: RequestSpecification
) {
    @Nested
    inner class SecuredByAnnotationOnController : BookResourceFixture(spec, "/books")

    @Nested
    inner class SecuredByInterceptUrlMap : BookResourceFixture(spec, "/books/url-not-secured")

    abstract inner class BookResourceFixture(
        private val spec: RequestSpecification,
        private val path: String
    ) {
        @Test
        fun `returns 401 UNAUTHORIZED if authentication is missing`() {
            spec
                .get(path)
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 401 UNAUTHORIZED if user name is incorrect`() {
            spec
                .basicAuth("unknown User", "")
                .get(path)
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 401 UNAUTHORIZED if password is incorrect`() {
            spec
                .basicAuth("reader", "NOT reader's secret")
                .get(path)
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 200 OK if authenticated and authorized`() {
            spec
                .basicAuth("reader", "reader's secret")
                .get(path)
                .then()
                .statusCode(200)
        }

        @Test
        fun `returns 403 FORBIDDEN if not authorized`() {
            spec
                .basicAuth("reader", "reader's secret")
                .jsonBody(
                    """{
                     "title": "Some title",
                     "isbn":  "0123456789123"
                   }""".trimIndent()
                )
                .post(path)
                .then()
                .statusCode(403)
        }

        @Test
        fun `returns 400 BAD REQUEST for a malformed request body`() {
            spec
                .basicAuth("writer", "writer's secret")
                .jsonBody(
                    """{
                     "a body": "that is invalid" 
                   }""".trimIndent()
                )
                .post(path)
                .then()
                .statusCode(400)
        }

        @Test
        fun `returns 201 CREATED if authorized`() {
            spec
                .basicAuth("writer", "writer's secret")
                .jsonBody(
                    """{
                     "title": "Some title",
                     "isbn":  "0123456789123"
                   }""".trimIndent()
                )
                .post(path)
                .then()
                .statusCode(201)
        }

        private fun RequestSpecification.basicAuth(
            username: String,
            password: String
        ): RequestSpecification {
            return this
                .auth()
                // directly sends authorization header to prevent additional round trip
                .preemptive()
                .basic(username, password)
        }

        private fun RequestSpecification.jsonBody(body: String): RequestSpecification {
            // contentType is important.
            // If you do not specify it, Micronaut Security will not match the request to an endpoint
            // and will always return a 403.
            return this.contentType(ContentType.JSON)
                .body(body)
        }
    }
}
