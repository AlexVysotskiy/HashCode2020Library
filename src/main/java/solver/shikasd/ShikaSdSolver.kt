package solver.shikasd

import common.*

class ShikaSdSolver(override val name: String = "shikasd.GreedySolver") : Solver {
    override fun solve(input: Input): Output {
        val takenBooks = BooleanArray(input.books.size)
        var booksLeft = input.books.size
        val libraries = ArrayList(input.libraries)
        val result = mutableListOf<Entry>()

        var tick = 0
        while (booksLeft > 0 && libraries.isNotEmpty()) {
            val nextLibrary = findLibrary(libraries, takenBooks)
            libraries.remove(nextLibrary)
            val scannedBooks = nextLibrary.books.filter { !takenBooks[it.id] }
            scannedBooks.forEach { takenBooks[it.id] = true }
            if (scannedBooks.isEmpty()) {
                continue
            }
            result.add(
                Entry(
                    nextLibrary,
                    scannedBooks
                )
            )
            booksLeft -= scannedBooks.size
        }

        return Output(result)
    }

    private fun findLibrary(libraries: MutableList<Library>, takenBooks: BooleanArray): Library =
        libraries.minBy { it.signup }!!
}
