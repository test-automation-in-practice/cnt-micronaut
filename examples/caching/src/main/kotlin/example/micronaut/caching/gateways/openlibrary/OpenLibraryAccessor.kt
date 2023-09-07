package example.micronaut.caching.gateways.openlibrary

import io.micronaut.cache.annotation.Cacheable
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory.getLogger
import java.io.IOException

@Singleton
open class OpenLibraryAccessor(private val client: OpenLibraryClient) {

    private val log = getLogger(javaClass)

    @Cacheable(value = ["get-number-of-pages-by-isbn"], parameters = ["isbn"])
    open fun getNumberOfPages(isbn: String): Int? = try {
        client.getNumberOfPages(isbn)
    } catch (ex: IOException) {
        log.error("Unable to get number of pages for ISBN [$isbn] because of an exception: ${ex.message}", ex)
        null
    }
}
