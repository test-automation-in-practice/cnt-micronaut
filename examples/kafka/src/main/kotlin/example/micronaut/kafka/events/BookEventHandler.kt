package example.micronaut.kafka.events

import example.micronaut.kafka.business.BookCreatedEvent
import example.micronaut.kafka.business.BookDeletedEvent
import example.micronaut.kafka.business.BookEvent
import io.micronaut.configuration.kafka.annotation.ErrorStrategy
import io.micronaut.configuration.kafka.annotation.ErrorStrategyValue
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.configuration.kafka.exceptions.DefaultKafkaListenerExceptionHandler
import io.micronaut.configuration.kafka.exceptions.KafkaListenerException
import io.micronaut.configuration.kafka.exceptions.KafkaListenerExceptionHandler
import io.micronaut.context.annotation.Replaces
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory.getLogger

@KafkaListener(
    offsetReset = EARLIEST,
    errorStrategy = ErrorStrategy(
        value = ErrorStrategyValue.RESUME_AT_NEXT_RECORD,
    )
)
open class BookEventHandler {

    private val log = getLogger(javaClass)

    @Topic("book-created")
    open fun handleCreatedEvent(event: BookCreatedEvent) {
        log.info("”Book was created: $event")
    }

    @Topic("book-deleted")
    open fun handleDeletedEvent(event: BookDeletedEvent) {
        log.info("Book was deleted: $event")
    }
}

@Singleton
@Replaces(DefaultKafkaListenerExceptionHandler::class)
class KafkaListenerExceptionHandlerFactory(
    private val publishEvent: PublishEventFunction
) : KafkaListenerExceptionHandler {

    private val log = getLogger(javaClass)

    override fun handle(exception: KafkaListenerException?) {
        log.error("Failed to publish record due to: {}", exception?.message, exception)

        exception?.consumerRecord?.ifPresent { record ->
            when (val event = record.value()) {
                is BookEvent -> publishEvent.bookDeadLetter(event)
                else -> log.info("Cannot process events that are not of type ${BookEvent::class.simpleName}.")
            }
        }
    }
}