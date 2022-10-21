package example.micronaut.httpserver.books.enrichment

interface BookInformationSource {
    fun getBookInformation(isbn: String): BookInformation?
}
