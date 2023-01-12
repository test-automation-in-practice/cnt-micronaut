package example.micronaut.caching.openlibrary

import example.micronaut.caching.gateways.openlibrary.OpenLibraryAccessor
import example.micronaut.caching.gateways.openlibrary.OpenLibraryClient
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException

internal class OpenLibraryAccessorTest {

    private val isbn = "9781680680584"

    private val client: OpenLibraryClient = mockk()
    private val cut = OpenLibraryAccessor(client)

    @Nested
    inner class GetNumberOfPages {

        @Test
        fun `returns number of pages if found`() {
            every { client.getNumberOfPages(isbn) } returns 308

            val actual = cut.getNumberOfPages(isbn)

            actual shouldBe 308
        }

        @Test
        fun `returns null if null was returned`() {
            every { client.getNumberOfPages(isbn) } returns null

            val actual = cut.getNumberOfPages(isbn)

            actual.shouldBeNull()
        }

        @Test
        fun `returns null on IOException`() {
            every { client.getNumberOfPages(isbn) } throws IOException("oops")

            val actual = cut.getNumberOfPages(isbn)

            actual.shouldBeNull()
        }
    }
}
