package example.micronaut.security.core

import java.util.UUID

data class Book(
    val id: UUID,
    val isbn: String,
    val title: String,
)
