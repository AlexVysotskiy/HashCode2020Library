package solver.shikasd

import common.*
import kotlin.math.ceil
import kotlin.math.min

class ShikaSdSolver(override val name: String = "shikasd.GreedySolver") : Solver {
    override fun solve(input: Input): Output {
        val takenBooks = BooleanArray(input.books.size)
        var booksLeft = input.books.size
        val libraries = ArrayList(input.libraries)
        val result = mutableListOf<Entry>()

        var tick = 0
        while (booksLeft > 0 && libraries.isNotEmpty()) {
            val nextLibrary = findLibrary(libraries, takenBooks, tick, input.days)
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
            tick += nextLibrary.signup
        }

        return Output(result)
    }

    private fun findLibrary(libraries: MutableList<Library>, takenBooks: BooleanArray, tick: Int, deadline: Int): Library =
        libraries.maxBy {
            val newBooks = it.books.count { !takenBooks[it.id] }
            val q = newBooks.toFloat()
            val v = it.shippingRate
            val s = it.books.sumBy { if (takenBooks[it.id]) 0 else it.score }
            val d = deadline
            val r = it.signup
            val c = tick
            val k = 1 - min(r / (d - c).toFloat(), 1f)

            val score = (min(v / q, 1f) * s) * (min(ceil(q / v).toInt(), d - r - c)) * k
            score
        }!!
}
