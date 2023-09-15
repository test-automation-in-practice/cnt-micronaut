package example.data.jpa.persistence

import example.data.jpa.model.Title
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.UUID

@Repository
interface BookEntityRepository : JpaRepository<BookEntity, UUID> {
    @Query("SELECT b FROM BookEntity b WHERE b.book.title = :title")
    fun findByTitle(title: Title): List<BookEntity>
}
