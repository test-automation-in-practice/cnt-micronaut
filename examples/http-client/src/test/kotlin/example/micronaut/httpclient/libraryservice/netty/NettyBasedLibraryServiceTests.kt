package example.micronaut.httpclient.libraryservice.netty

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import example.micronaut.httpclient.gateways.libraryservice.LibraryService
import example.micronaut.httpclient.gateways.libraryservice.LibraryServiceProperties
import example.micronaut.httpclient.gateways.libraryservice.netty.NettyClientConfiguration
import example.micronaut.httpclient.libraryservice.LibraryServiceContract
import io.micronaut.http.client.HttpClient

@WireMockTest
internal class NettyBasedLibraryServiceTests(wiremockInfo: WireMockRuntimeInfo) :
    LibraryServiceContract(wiremockInfo) {

    override fun createClassUnderTest(properties: LibraryServiceProperties): LibraryService {
        return NettyClientConfiguration(HttpClient.create(null), properties).libraryService()
    }
}