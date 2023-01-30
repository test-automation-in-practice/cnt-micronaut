package example.micronaut.rabbitmq.messaging

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import example.micronaut.rabbitmq.business.Examples
import example.micronaut.rabbitmq.business.createdEvent
import example.micronaut.rabbitmq.business.deletedEvent
import example.micronaut.rabbitmq.events.DeadLetterHandler
import example.micronaut.rabbitmq.events.EventHandler
import example.micronaut.rabbitmq.events.PublishEventFunction
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

@MicronautTest
class MessagingIntegrationTest(
    private val publishEvent: PublishEventFunction,
    private val eventHandler: EventHandler,
    private val deadLetterHandler: DeadLetterHandler,
    private val channel: Channel,
) {

    @MockBean(EventHandler::class)
    fun mockEventHandler() = mockk<EventHandler>(relaxed = true)

    @MockBean(DeadLetterHandler::class)
    fun mockDeadLetterHandler() = mockk<DeadLetterHandler>(relaxed = true)

    private val createdEvent = Examples.cleanCode.createdEvent()
    private val deletedEvent = Examples.cleanCode.deletedEvent()

    @Test
    fun `BookCreatedEvents are dispatched and received`() {
        publishEvent(createdEvent)

        verify(timeout = 1_000) { eventHandler.handleCreatedEvent(createdEvent) }
        verify(exactly = 0) { eventHandler.handleDeletedEvent(any()) }
    }

    @Test
    fun `BookDeletedEvents are dispatched and received`() {
        publishEvent(deletedEvent)

        verify(timeout = 1_000) { eventHandler.handleDeletedEvent(deletedEvent) }
        verify(exactly = 0) { eventHandler.handleCreatedEvent(any()) }
    }

    @Test
    fun `dead letter queue is used in case of an exception`() {
        every { eventHandler.handleDeletedEvent(deletedEvent) } throws RuntimeException("upsi")

        publishEvent(deletedEvent)

        verify(timeout = 1_000) { eventHandler.handleDeletedEvent(deletedEvent) }
        verify(timeout = 1_000) { deadLetterHandler.handleDeadLetter(any()) }
    }

    @Test
    fun `dead letter queue is used in case a message cannot be read`() {
        channel.basicPublish(
            "exchanges.events",
            "book-created",
            AMQP.BasicProperties(),
            "{ \"foo\": \"bar\"}".toByteArray()
        )

        verify(timeout = 1_000) { deadLetterHandler.handleDeadLetter(any()) }
    }
}