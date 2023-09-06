package example.micronaut.httpserver

import java.util.UUID

class IdGenerator {
    fun generateId(): UUID = UUID.randomUUID()
}
