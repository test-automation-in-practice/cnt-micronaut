package example.micronaut.rabbitmq.messaging

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.DEAD_LETTERS_CONFIG
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Exchanges.EXCHANGE_EVENTS
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Queues.QUEUE_BOOK_CREATED
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Queues.QUEUE_BOOK_DELETED
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.RoutingKeys.ROUTING_KEY_BOOK_CREATED
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.RoutingKeys.ROUTING_KEY_BOOK_DELETED
import io.micronaut.rabbitmq.connect.ChannelInitializer
import jakarta.inject.Singleton


@Singleton
class EventQueueConfiguration : ChannelInitializer() {

    override fun initialize(channel: Channel, name: String) {
        channel.apply {
            exchangeDeclare(EXCHANGE_EVENTS, BuiltinExchangeType.DIRECT, true)

            queueDeclare(QUEUE_BOOK_CREATED, true, false, false, DEAD_LETTERS_CONFIG)
            queueDeclare(QUEUE_BOOK_DELETED, true, false, false, DEAD_LETTERS_CONFIG)

            queueBind(QUEUE_BOOK_CREATED, EXCHANGE_EVENTS, ROUTING_KEY_BOOK_CREATED)
            queueBind(QUEUE_BOOK_DELETED, EXCHANGE_EVENTS, ROUTING_KEY_BOOK_DELETED)
        }
    }
}