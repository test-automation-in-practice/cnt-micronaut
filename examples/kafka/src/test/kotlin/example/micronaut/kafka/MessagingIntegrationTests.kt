package example.micronaut.kafka

import example.micronaut.kafka.business.createdEvent
import example.micronaut.kafka.business.deletedEvent
import example.micronaut.kafka.events.DeadLetterHandler
import example.micronaut.kafka.events.BookEventHandler
import example.micronaut.kafka.events.PublishEventFunction
import example.spring.boot.kafka.business.Examples.cleanCode
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

@MicronautTest
class MessagingIntegrationTests(
    private val publishEvent: PublishEventFunction,
    private val bookEventHandler: BookEventHandler,
    private val deadLetterHandler: DeadLetterHandler
) {

    @MockBean(BookEventHandler::class)
    fun mockEventHandler() = mockk<BookEventHandler>(relaxed = true)

    @MockBean(DeadLetterHandler::class)
    fun mockDeadLetterHandler() = spyk<DeadLetterHandler>()

    @Test
    fun `handles BookCreatedEvent`() {
        val event = cleanCode.createdEvent()

        publishEvent(event)

        verify(timeout = 3_000) { bookEventHandler.handleCreatedEvent(event) }
    }

    @Test
    fun `handles BookDeletedEvent`() {
        val event = cleanCode.deletedEvent()

        publishEvent(event)

        verify(timeout = 3_000) { bookEventHandler.handleDeletedEvent(event) }
    }

    @Test
    fun `puts failure messages to dead letter queue`(){
        every { bookEventHandler.handleCreatedEvent(any()) } throws IllegalArgumentException("upsi")
        val event = cleanCode.createdEvent() // used as representation

        publishEvent(event)

        verify(timeout = 3_000) { deadLetterHandler.handleDeadLetterEvent(any()) }
    }
}