package example.micronaut.basics.events

import example.micronaut.basics.Examples.record_cleanCode
import example.micronaut.basics.Examples.id_cleanCode
import example.micronaut.basics.books.core.BookEvent
import example.micronaut.basics.books.core.BookRecordCreatedEvent
import example.micronaut.basics.books.core.BookRecordDeletedEvent
import io.micronaut.context.event.ApplicationEventPublisher
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class InMemoryEventPublisherTest {

    private val delegate: ApplicationEventPublisher<BookEvent> = mockk(relaxed = true)
    private val cut = InMemoryEventPublisher(delegate)

    @BeforeEach
    fun resetMocks() {
        clearMocks(delegate)
    }

    @TestFactory
    fun `all book events are simply published as application events`() = listOf(
        BookRecordCreatedEvent(record_cleanCode), BookRecordDeletedEvent(id_cleanCode)
    ).map { event ->
        dynamicTest(event::class.simpleName) {
            cut.publish(event)
            verify { delegate.publishEvent(event) }
        }
    }
}
