package example.micronaut.rabbitmq.events

import example.micronaut.rabbitmq.messaging.QueueConfigurationValues.Queues.QUEUE_DEAD_LETTERS
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import org.slf4j.LoggerFactory

@RabbitListener
open class DeadLetterHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @Queue(QUEUE_DEAD_LETTERS)
    open fun handleDeadLetter(message: Any) {
        log.error("Dead letter message arrived: $message")
    }
}
