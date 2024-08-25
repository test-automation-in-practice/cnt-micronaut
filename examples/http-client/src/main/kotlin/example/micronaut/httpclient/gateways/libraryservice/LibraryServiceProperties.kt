package example.micronaut.httpclient.gateways.libraryservice

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("library-service")
internal class LibraryServiceProperties(
    var baseUrl: String
)
