package example.micronaut.security.api

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest
class BooksResourceJwtAuthTest(
    private val spec: RequestSpecification
) {

    @Nested
    inner class BearerCreation {
        @Test
        fun `returns 401 UNAUTHORIZED if username is unknown`() {
            spec
                .withLoginPayload("unknown User", "wrong password")
                .post("/login")
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 401 UNAUTHORIZED if password is incorrect`() {
            spec
                .withLoginPayload("reader", "wrong password")
                .post("/login")
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 400 BAD REQUEST if credentials are missing`() {
            spec
                .jsonBody(
                    """{
                    }
                """.trimIndent()
                )
                .post("/login")
                .then()
                .statusCode(400)
        }

        @Test
        fun `returns 200 OK if credentials are valid`() {
            spec
                .withLoginPayload("reader", "reader's secret")
                .post("/login")
                .then()
                .statusCode(200)
        }
    }

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
        fun `returns 403 UNAUTHORIZED if bearer is invalid`() {
            spec
                .auth().oauth2("invalid token")
                .get(path)
                .then()
                .statusCode(401)
        }

        @Test
        fun `returns 200 OK if bearer is valid`() {
            spec
                .withBearerToken("reader", "reader's secret")
                .get(path)
                .then()
                .statusCode(200)
        }

        @Test
        fun `returns 403 FORBIDDEN if bearer misses authorization`() {
            spec
                .withBearerToken("reader", "reader's secret")
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
                .withBearerToken("writer", "writer's secret")
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
        fun `returns 201 CREATED if bearer has necessary authorization`() {
            spec
                .withBearerToken("writer", "writer's secret")
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

        private fun RequestSpecification.withBearerToken(
            username: String,
            password: String
        ): RequestSpecification {
            val accessToken = this
                .withLoginPayload(username, password)
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .path<String>("access_token")

            return this.withBearerToken(accessToken)
        }

        private fun RequestSpecification.withBearerToken(accessToken: String): RequestSpecification {
            return this
                .auth()
                .oauth2(accessToken)
        }
    }

    private fun RequestSpecification.withLoginPayload(
        username: String,
        password: String
    ): RequestSpecification {
        return this
            .jsonBody(
                """
                    {
                      "username": "$username",
                      "password": "$password"
                    }
                """.trimIndent()
            )
    }

    private fun RequestSpecification.jsonBody(body: String): RequestSpecification {
        // contentType is important.
        // If you do not specify it, Micronaut Security will not match the request to an endpoint
        // and will always return a 403.
        return this.contentType(ContentType.JSON)
            .body(body)
    }
}
