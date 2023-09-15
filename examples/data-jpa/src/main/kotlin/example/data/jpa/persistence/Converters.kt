package example.data.jpa.persistence

import example.data.jpa.model.Isbn
import example.data.jpa.model.Title
import jakarta.inject.Singleton
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Singleton
@Converter
class TitleConverter : AttributeConverter<Title, String> {
    override fun convertToDatabaseColumn(attribute: Title?) = attribute?.value

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let(::Title)
}

@Singleton
@Converter
class IsbnConverter : AttributeConverter<Isbn, String> {
    override fun convertToDatabaseColumn(attribute: Isbn?) = attribute?.value

    override fun convertToEntityAttribute(dbData: String?) = dbData?.let(::Isbn)
}
