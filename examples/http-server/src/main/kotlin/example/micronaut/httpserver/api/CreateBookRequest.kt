package example.micronaut.httpserver.api

import io.micronaut.core.annotation.Introspected
import jakarta.validation.constraints.Pattern

@Introspected // necessary for jakarta-validation to work at runtime
data class CreateBookRequest(
    val title: String,
    @field:Pattern(regexp = "[0-9]{13}")
    val isbn: String
)
