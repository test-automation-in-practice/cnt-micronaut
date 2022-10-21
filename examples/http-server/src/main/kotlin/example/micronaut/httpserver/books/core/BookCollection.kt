package example.micronaut.httpserver.books.core

import example.micronaut.httpserver.IdGenerator
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory.getLogger
import java.util.*

@Singleton
class BookCollection(
    private val idGenerator: IdGenerator,
    private val repository: BookRepository,
) {

    private val log = getLogger(javaClass)

    // a method that takes an input and produces an output, but also has a side effect

    fun add(book: Book): BookRecord {
        val record = BookRecord(idGenerator.generateId(), book)
        return repository.save(record)
    }

    // a method that simply delegates to a less abstract component

    fun get(id: UUID): BookRecord? = repository.findById(id)

    fun getAll(): Collection<BookRecord> = repository.getAll()

    // a method whose behaviour is determined by a dependency and that does not return anything

    fun delete(id: UUID) {
        log.info("trying to delete book with ID '$id'")
        if (repository.deleteById(id)) {
            log.debug("book with ID '$id' was deleted")
        } else {
            log.debug("book with ID '$id' was not deleted")
        }
    }

}
