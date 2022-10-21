package example.micronaut.httpserver.api

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Pattern

@Introspected // necessary for javax-validation to work at runtime
data class CreateBookRequest(
    val title: String,
    @field:Pattern(regexp = "[0-9]{13}")
    val isbn: String
)