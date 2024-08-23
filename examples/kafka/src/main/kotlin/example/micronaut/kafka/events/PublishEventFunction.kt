package example.micronaut.kafka.events

import example.micronaut.kafka.business.BookCreatedEvent
import example.micronaut.kafka.business.BookDeletedEvent
import example.micronaut.kafka.business.BookEvent
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface PublishEventFunction {

    @Topic("book-created")
    operator fun invoke(event: BookCreatedEvent)

    @Topic("book-deleted")
    operator fun invoke(event: BookDeletedEvent)

    @Topic("book-dead-letter")
    fun bookDeadLetter(event: BookEvent)
}
