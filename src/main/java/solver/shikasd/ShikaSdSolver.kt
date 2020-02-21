package solver.shikasd

import common.*
import common.helpers.SwarmOptimizer
import common.score.kotlin.ScoreCalculatorImpl
import kotlin.math.ceil
import kotlin.math.min

class ShikaSdSolver(override val name: String = "shikasd.GreedySolver") : Solver {
    override fun solve(input: Input): Output {
        val calculator = ScoreCalculatorImpl()
        val solution = SwarmOptimizer(
            FloatArray(4) { 1f },
            SwarmOptimizer.Params(
                maxIterationCount = 100,
                particleCount = 50,
                maxX = 100f
            ),
            calculateScore = { calculator.calculateResult(input, doSolve(input, it)).toInt() },
            saver = { params, score -> println(params.toList()) }
        )


//        return doSolve(input, solution.solve())
        return doSolve(input, floatArrayOf(3.918113f, 2.9230165f, 2.7732782f, 4.474831f))
    }

    private fun doSolve(input: Input, params: FloatArray): Output {
        val takenBooks = BooleanArray(input.books.size)
        var booksLeft = input.books.size
        val libraries = ArrayList(input.libraries)
        val result = mutableListOf<Entry>()

        var tick = 0
        while (booksLeft > 0 && libraries.isNotEmpty()) {
            val nextLibrary = findLibrary(libraries, takenBooks, tick, input.days, params)
            libraries.remove(nextLibrary)
            val daysLeft = input.days - (tick + nextLibrary.signup)
            val canRead = Math.max(0, daysLeft * nextLibrary.shippingRate)
            val scannedBooks = nextLibrary.books
                .asSequence()
                .filter { !takenBooks[it.id] }
                .sortedBy { -it.score }
                .take(canRead)
                .toList()

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
            if (tick > input.days) {
                break
            }
        }

        return Output(result)
    }

    private fun findLibrary(libraries: MutableList<Library>, takenBooks: BooleanArray, tick: Int, deadline: Int, params: FloatArray): Library {
        var maxLibrary: Library? = null
        var maxScore = Float.NEGATIVE_INFINITY
        for (i in libraries.indices) {
            val library = libraries[i]
            var newBooksCount = 0
            var newBooksSum = 0
            for (j in library.books.indices) {
                val book = library.books[j]
                if (!takenBooks[book.id]) {
                    newBooksSum += book.score
                    newBooksCount++
                }
            }
            val q = newBooksCount * params[2]
            val v = library.shippingRate * params[3]
            val s = newBooksSum * params[1]
            val d = deadline
            val r = library.signup * params[0]
            val c = tick
            val k = 1 - min(r / (d - c).toFloat(), 1f)

            val score = (min(v / q, 1f) * s) * (min(ceil(q / v).toInt(), d - r.toInt() - c)) * k * k * k * (1 / r) * q
            if (score > maxScore) {
                maxScore = score
                maxLibrary = library
            }
        }
        return maxLibrary!!
    }
}
