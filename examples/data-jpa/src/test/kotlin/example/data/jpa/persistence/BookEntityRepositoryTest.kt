package example.data.jpa.persistence

import example.data.jpa.model.Book
import example.data.jpa.model.Isbn
import example.data.jpa.model.Title
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.random.Random.Default.nextInt
import java.util.UUID

@MicronautTest
class BookEntityRepositoryTest(
    private val cut: BookEntityRepository
) {

    @Test
    fun `entity can be saved`() {
        val entity = bookRecordEntity()

        val savedEntity = cut.save(entity)

        savedEntity.shouldBe(entity)
    }

    @Test
    fun `entity version is increased with every change`() {
        // Micronaut never returns a new Object after save, but applies all changes in place
        // @Version functionality is only triggered after a flush hence we call saveAndFlush
        val entity = bookRecordEntity()

        val savedEntity1 = cut.saveAndFlush(entity)
        savedEntity1.version.shouldBe(0)

        val savedEntity2 = cut.saveAndFlush(savedEntity1.changeTitle())
        savedEntity2.version.shouldBe(1)

        val savedEntity3 = cut.saveAndFlush(savedEntity2)
        savedEntity3.version.shouldBe(1)

        val savedEntity4 = cut.saveAndFlush(savedEntity3.changeTitle())
        savedEntity4.version.shouldBe(2)
    }

    @Test
    @Disabled
    // TODO Any idea whether how to test this? It is not easy, as Micronaut only has a single object
    // with the same Identifier.
    fun `entity can not be saved in lower than current version`() {
        val entity = bookRecordEntity()
        val savedEntity1 = cut.save(entity)
        cut.save(savedEntity1.changeTitle())

        shouldThrowExactly<IllegalArgumentException> { cut.save(savedEntity1) }
    }

    @Test
    fun `entity can be found by id`() {
        val savedEntity = cut.save(bookRecordEntity())

        val foundEntity = cut.findById(savedEntity.id)

        foundEntity.shouldBePresent().shouldBe(savedEntity)
    }

    @Test
    fun `entity can be found by title`() {
        val e1 = cut.save(bookRecordEntity("Clean Code"))
        cut.save(bookRecordEntity("Clean Architecture"))
        val e3 = cut.save(bookRecordEntity("Clean Code"))

        val foundEntities = cut.findByTitle(Title("Clean Code"))

        foundEntities.shouldContainExactlyInAnyOrder(e1, e3)
    }

    private fun bookRecordEntity(title: String = "Clean Code") =
        BookEntity(UUID.randomUUID(), Book(Isbn("9780123456789"), Title(title)))

    private fun BookEntity.changeTitle(): BookEntity =
        apply { book = book.copy(title = Title("Change Title #${nextInt(1_000)}")) }
}
