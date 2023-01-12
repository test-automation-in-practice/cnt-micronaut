package example.micronaut.caching.gateways.openlibrary

import java.io.IOException

interface OpenLibraryClient {

    @Throws(IOException::class)
    fun getNumberOfPages(isbn: String): Int?
}