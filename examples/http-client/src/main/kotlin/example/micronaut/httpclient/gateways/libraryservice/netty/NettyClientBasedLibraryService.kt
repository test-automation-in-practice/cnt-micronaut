package example.micronaut.httpclient.gateways.libraryservice.netty

import example.micronaut.httpclient.gateways.libraryservice.Book
import example.micronaut.httpclient.gateways.libraryservice.CreatedBook
import example.micronaut.httpclient.gateways.libraryservice.LibraryService
import example.micronaut.httpclient.gateways.libraryservice.LibraryServiceException
import example.micronaut.httpclient.gateways.libraryservice.LibraryServiceProperties
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import java.net.URI

internal class NettyClientBasedLibraryService(
    private val httpClient: HttpClient,
    private val properties: LibraryServiceProperties
) : LibraryService {

    override fun addBook(book: Book): CreatedBook {
        val post = HttpRequest
            .POST(URI.create("${properties.baseUrl}/api/books"), book)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)

        return try {
            httpClient
                .toBlocking()
                .retrieve(post, CreatedBook::class.java)
                ?: throw LibraryServiceException("Unexpected 'null' response")
        } catch (exception: HttpClientResponseException) {
            throw LibraryServiceException(cause = exception)
        }
    }
}
