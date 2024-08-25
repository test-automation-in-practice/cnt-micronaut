package example.micronaut.httpclient.gateways.libraryservice

data class Book(val title: String, val isbn: String)
data class CreatedBook(val id: String, val title: String, val isbn: String)
