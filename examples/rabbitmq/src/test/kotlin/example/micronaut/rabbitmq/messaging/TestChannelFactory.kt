package example.micronaut.rabbitmq.messaging

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import java.net.URI

@Factory
class TestChannelFactory {

    @Singleton
    fun channel(@Value("\${rabbitmq.uri}") uri: String): Channel {
        return ConnectionFactory().apply { setUri(URI(uri)) }.newConnection().createChannel()
    }
}