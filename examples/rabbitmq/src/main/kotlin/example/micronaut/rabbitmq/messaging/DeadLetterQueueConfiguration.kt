package example.micronaut.rabbitmq.messaging

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Exchanges.EXCHANGE_DEAD_LETTERS
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Queues.QUEUE_DEAD_LETTERS
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.RoutingKeys.ROUTING_KEY_DEAD_LETTERS
import io.micronaut.rabbitmq.connect.ChannelInitializer
import jakarta.inject.Singleton

@Singleton
class DeadLetterQueueConfiguration : ChannelInitializer() {

    override fun initialize(channel: Channel, name: String) {
        channel.apply {
            exchangeDeclare(EXCHANGE_DEAD_LETTERS, BuiltinExchangeType.DIRECT, true)

            queueDeclare(QUEUE_DEAD_LETTERS, true, false, false, mutableMapOf())

            queueBind(QUEUE_DEAD_LETTERS, EXCHANGE_DEAD_LETTERS, ROUTING_KEY_DEAD_LETTERS)
        }
    }
}
