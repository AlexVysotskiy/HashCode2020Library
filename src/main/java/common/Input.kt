package common

data class Input(
    val days: Int,
    val books: List<Book>,
    val libraries: List<Library>
)

data class Book(
    val id: Int,
    val score: Int
)

data class Library(
    val signup: Int,
    val shippingRate: Int,
    val books: List<Book>
)
