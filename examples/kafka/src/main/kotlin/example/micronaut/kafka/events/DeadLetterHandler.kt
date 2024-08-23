package example.micronaut.kafka.events

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory.getLogger

@KafkaListener
open class DeadLetterHandler {

    private val log = getLogger(javaClass)

    @Topic("book-dead-letter")
    open fun handleDeadLetterEvent(record: ConsumerRecord<String, *>) {
        log.error("Dead letter message arrived: ${record.value()}")
    }
}