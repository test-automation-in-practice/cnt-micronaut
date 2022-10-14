package example.micronaut.basics.books.enrichment

interface BookInformationSource {
    fun getBookInformation(isbn: String): BookInformation?
}
