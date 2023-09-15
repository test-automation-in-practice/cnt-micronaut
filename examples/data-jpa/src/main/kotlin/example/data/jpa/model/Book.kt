package example.data.jpa.model

import example.data.jpa.persistence.IsbnConverter
import example.data.jpa.persistence.TitleConverter
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@NoArgConstructor
@Embeddable
data class Book(
    @field:Convert (converter = IsbnConverter::class)
    val isbn: Isbn,
    @field:Convert (converter = TitleConverter::class)
    val title: Title
)
