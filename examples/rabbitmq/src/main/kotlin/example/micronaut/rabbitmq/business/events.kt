package example.micronaut.rabbitmq.business

import example.spring.boot.rabbitmq.business.Book
import example.spring.boot.rabbitmq.business.BookRecord
import java.util.*
import java.util.UUID.randomUUID

sealed class BookEvent {
    abstract val type: String
    abstract val eventId: UUID
    abstract val bookId: UUID
}

data class BookCreatedEvent(
    override val eventId: UUID,
    override val bookId: UUID,
    val book: Book
) : BookEvent() {
    override val type: String = "book-created"
}

data class BookDeletedEvent(
    override val eventId: UUID,
    override val bookId: UUID
) : BookEvent() {
    override val type: String = "book-deleted"
}

fun BookRecord.createdEvent() =
    BookCreatedEvent(
        eventId = randomUUID(),
        bookId = this.id,
        book = this.book
    )

fun BookRecord.deletedEvent() =
    BookDeletedEvent(
        eventId = randomUUID(),
        bookId = this.id
    )
