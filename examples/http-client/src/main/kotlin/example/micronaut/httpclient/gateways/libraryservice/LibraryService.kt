package example.micronaut.httpclient.gateways.libraryservice

interface LibraryService {

    @Throws(LibraryServiceException::class)
    fun addBook(book: Book): CreatedBook

}
