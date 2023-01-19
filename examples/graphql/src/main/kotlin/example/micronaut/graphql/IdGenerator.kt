package example.micronaut.graphql

import jakarta.inject.Singleton
import java.util.*

@Singleton
class IdGenerator {
    fun generateId(): UUID = UUID.randomUUID()
}
