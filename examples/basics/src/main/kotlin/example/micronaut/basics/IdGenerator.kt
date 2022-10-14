package example.micronaut.basics

import java.util.*

class IdGenerator {
    fun generateId(): UUID = UUID.randomUUID()
}
