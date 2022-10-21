package example.micronaut.httpserver.persistence

import example.micronaut.httpserver.books.core.BookRecord
import example.micronaut.httpserver.books.core.BookRepository
import jakarta.inject.Singleton
import java.time.Clock
import java.util.UUID

@Singleton
class InMemoryBookRepository(
    private val clock: Clock
) : BookRepository {

    private val database = mutableMapOf<UUID, BookRecord>()

    override fun save(record: BookRecord): BookRecord {
        val modifiedRecord = record.copy(timestamp = clock.instant())
        database[record.id] = modifiedRecord
        return modifiedRecord
    }

    override fun findById(id: UUID): BookRecord? {
        return database[id]
    }

    override fun getAll(): Collection<BookRecord> {
        return database.values
    }

    override fun deleteById(id: UUID): Boolean {
        return database.remove(id) != null
    }
}
