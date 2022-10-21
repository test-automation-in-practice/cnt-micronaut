package example.micronaut.httpserver

import java.util.*

class IdGenerator {
    fun generateId(): UUID = UUID.randomUUID()
}
