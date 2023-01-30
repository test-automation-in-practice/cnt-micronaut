package example.micronaut.rabbitmq.messaging

import example.micronaut.rabbitmq.messaging.QueueConfigurationValues.Exchanges.EXCHANGE_DEAD_LETTERS
import example.micronaut.rabbitmq.messaging.QueueConfigurationValues.RoutingKeys.ROUTING_KEY_DEAD_LETTERS

object QueueConfigurationValues {

    object Exchanges {
        private const val EXCHANGES_PREFIX = "exchanges"

        const val EXCHANGE_EVENTS = "$EXCHANGES_PREFIX.events"
        const val EXCHANGE_DEAD_LETTERS = "$EXCHANGES_PREFIX.dead-letters"
    }

    object Queues {

        private const val QUEUES_PREFIX = "queues"

        const val QUEUE_BOOK_CREATED = "$QUEUES_PREFIX.events.book-created"
        const val QUEUE_BOOK_DELETED = "$QUEUES_PREFIX.events.book-deleted"
        const val QUEUE_DEAD_LETTERS =  "$QUEUES_PREFIX.dead-letters"
    }

    object RoutingKeys {

        const val ROUTING_KEY_DEAD_LETTERS = "dead-letter"
        const val ROUTING_KEY_BOOK_CREATED = "book-created"
        const val ROUTING_KEY_BOOK_DELETED = "book-deleted"
    }

    val DEAD_LETTERS_CONFIG: MutableMap<String, Any> =
        mutableMapOf(
            "x-dead-letter-exchange" to EXCHANGE_DEAD_LETTERS,
            "x-dead-letter-routing-key" to ROUTING_KEY_DEAD_LETTERS
        )

}