package example.micronaut.httpclient.gateways.libraryservice.netty

import example.micronaut.httpclient.gateways.libraryservice.LibraryService
import example.micronaut.httpclient.gateways.libraryservice.LibraryServiceProperties
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client

/**
 * As we're using "io.micronaut:micronaut-http-client", we're using
 * Netty under the hood.
 */
@Factory
internal class NettyClientConfiguration(
    @param:Client(id = "library") private val client: HttpClient,
    private val properties: LibraryServiceProperties
) {

    @Bean
    fun libraryService(): LibraryService {
        return NettyClientBasedLibraryService(client, properties)
    }
}
