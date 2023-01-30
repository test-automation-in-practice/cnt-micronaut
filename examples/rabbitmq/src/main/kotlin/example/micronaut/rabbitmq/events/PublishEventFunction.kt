package example.micronaut.rabbitmq.events

import example.micronaut.rabbitmq.business.BookCreatedEvent
import example.micronaut.rabbitmq.business.BookDeletedEvent
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.Exchanges.EXCHANGE_EVENTS
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.RoutingKeys.ROUTING_KEY_BOOK_CREATED
import example.micronaut.rabbitmq.messaging.MessagingConfiguration.RoutingKeys.ROUTING_KEY_BOOK_DELETED
import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient

@RabbitClient(EXCHANGE_EVENTS)
interface PublishEventFunction {

    @Binding(ROUTING_KEY_BOOK_CREATED)
    operator fun invoke(event: BookCreatedEvent)

    @Binding(ROUTING_KEY_BOOK_DELETED)
    operator fun invoke(event: BookDeletedEvent)
}
