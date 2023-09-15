package example.micronaut.security.infrastructure

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

/**
 * Provides a simple, in memory AuthenticationProvider that knows two pairs of credentials.
 */
@Singleton
class AuthenticationProviderUserPassword : AuthenticationProvider<HttpRequest<*>> {
    private val knownCredentials = mapOf(
        "reader" to "reader's secret",
        "writer" to "writer's secret"
    )

    private val rightsPerUser = mapOf("writer" to listOf("BOOK_CREATOR"))

    override fun authenticate(
        httpRequest: HttpRequest<*>,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Mono.create {
            val username = authenticationRequest.identity.toString()
            val password = knownCredentials[username]
            if (authenticationRequest.secret == password) {
                val roles = rightsPerUser[username] ?: listOf()
                it.success(AuthenticationResponse.success(username, roles))
            } else {
                it.error(AuthenticationResponse.exception())
            }
        }
    }
}
