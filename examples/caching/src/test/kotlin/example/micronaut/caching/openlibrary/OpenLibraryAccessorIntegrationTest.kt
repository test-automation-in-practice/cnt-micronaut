package example.micronaut.caching.openlibrary

import example.micronaut.caching.gateways.openlibrary.OpenLibraryAccessor
import example.micronaut.caching.gateways.openlibrary.OpenLibraryClient
import io.micronaut.cache.CacheManager
import io.micronaut.context.annotation.Factory
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Singleton
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@MicronautTest(startApplication = false)
class OpenLibraryAccessorIntegrationTest(
    private val cut: OpenLibraryAccessor,
    private val client: OpenLibraryClient,
    private val cacheManager: CacheManager<*>
) {

    private val isbn = "9781680680584"

    @BeforeEach
    fun resetMockedClient() {
        clearMocks(client)
    }

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
        cut.getNumberOfPages(isbn)

        verify(exactly = 0) { client.getNumberOfPages(isbn) }
    }

    @Test
    @Order(3)
    fun `after cached entry has expired, Cache is not hit`() {
        // we simulate an expiration by manually invalidating the cache
        cacheManager.getCache("get-number-of-pages-by-isbn").invalidateAll()
        every { client.getNumberOfPages(isbn) } returns 390

        cut.getNumberOfPages(isbn)

        verify(exactly = 1) { client.getNumberOfPages(isbn) }
    }

    @Factory
    class OpenLibraryClientFactory {

        @Singleton
        fun createOpenLibraryClientMock(): OpenLibraryClient {
            return mockk<OpenLibraryClient>()
        }
    }
}
