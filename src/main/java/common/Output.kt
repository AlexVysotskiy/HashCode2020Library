package common

data class Output(
    val entries: List<Entry>
)

data class Entry(
    val library: Library,
    val scannedBooks: List<Book>
)
