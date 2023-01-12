package example.micronaut.caching.openlibrary

import example.micronaut.caching.gateways.openlibrary.OpenLibraryAccessor
import example.micronaut.caching.gateways.openlibrary.OpenLibraryClient
import io.micronaut.context.annotation.Property
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@MicronautTest(startApplication = false)
@Property(name = "micronaut.caches.get-number-of-pages-by-isbn.expire-after-write", value = "PT0.1s")
class OpenLibraryAccessorIntegrationTest {

    private val isbn = "9781680680584"

    @Inject
    lateinit var client: OpenLibraryClient

    @Inject
    lateinit var cut: OpenLibraryAccessor

    @MockBean(OpenLibraryClient::class)
    fun mockClient() = mockk<OpenLibraryClient>()

    @Test
    @Order(1)
    fun `first invocation does not hit Cache`() {
        every { client.getNumberOfPages(isbn) } returns 390

        cut.getNumberOfPages(isbn)

        verify(exactly = 1) { client.getNumberOfPages(isbn) }
    }

    @Test
    @Order(2)
    fun `second invocation hits Cache`() {
        every { client.getNumberOfPages(isbn) } returns 390

        cut.getNumberOfPages(isbn)

        verify(exactly = 0) { client.getNumberOfPages(isbn) }
    }

    @Test
    @Order(3)
    fun `after cached entry has expired, Cache is not hit`() {
        every { client.getNumberOfPages(isbn) } returns 390
        Thread.sleep(101) // wait until the cache automatically expires

        cut.getNumberOfPages(isbn)

        verify(exactly = 1) { client.getNumberOfPages(isbn) }
    }
}

