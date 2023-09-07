package example.micronaut.rabbitmq.events

import example.micronaut.rabbitmq.business.BookCreatedEvent
import example.micronaut.rabbitmq.business.BookDeletedEvent
import example.micronaut.rabbitmq.messaging.QueueConfigurationValues
import example.micronaut.rabbitmq.messaging.QueueConfigurationValues.Queues.QUEUE_BOOK_CREATED
import example.micronaut.rabbitmq.messaging.QueueConfigurationValues.Queues.QUEUE_BOOK_DELETED
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import org.slf4j.LoggerFactory.getLogger

@RabbitListener
open class EventHandler {

    private val log = getLogger(javaClass)

    @Queue(QUEUE_BOOK_CREATED)
    open fun handleCreatedEvent(event: BookCreatedEvent) {
        log.info("Book was created: $event")
    }

    @Queue(QUEUE_BOOK_DELETED)
    open fun handleDeletedEvent(event: BookDeletedEvent) {
        log.info("Book was deleted: $event")
    }
}
