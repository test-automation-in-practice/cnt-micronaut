package example.micronaut.basics.books.enrichment

import example.micronaut.basics.books.core.Book
import example.micronaut.basics.books.core.BookRecordCreatedEvent
import example.micronaut.basics.books.core.BookRepository
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.scheduling.annotation.Async
import jakarta.inject.Singleton

// This component's job relies on being invoked using Micronaut Framework mechanisms.

@Singleton
open class BookEnricher(
    private val bookInformationSource: BookInformationSource,
    private val bookRepository: BookRepository
) {

    @Async
    @EventListener
    open fun handle(event: BookRecordCreatedEvent) {
        val record = event.bookRecord
        val book = record.book
        val data = bookInformationSource.getBookInformation(book.isbn)

        if (data != null) {
            val enrichedBookRecord = record.copy(book = enrich(book, data))
            bookRepository.save(enrichedBookRecord)
        }
    }

    private fun enrich(originalBook: Book, enrichmentData: BookInformation) =
        originalBook.copy(
            description = enrichmentData.description ?: originalBook.description,
            authors = enrichmentData.authors.takeIf { it.isNotEmpty() } ?: originalBook.authors,
            numberOfPages = enrichmentData.numberOfPages ?: originalBook.numberOfPages
        )
}
