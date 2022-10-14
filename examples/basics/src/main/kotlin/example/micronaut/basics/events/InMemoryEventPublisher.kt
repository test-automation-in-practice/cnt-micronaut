package example.micronaut.basics.events

import example.micronaut.basics.books.core.BookEvent
import example.micronaut.basics.books.core.BookEventPublisher
import io.micronaut.context.event.ApplicationEventPublisher
import jakarta.inject.Singleton

@Singleton
class InMemoryEventPublisher(
    private val delegate: ApplicationEventPublisher<BookEvent>
) : BookEventPublisher {

    override fun publish(event: BookEvent) {
        delegate.publishEvent(event)
    }
}
