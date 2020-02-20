package common.score.kotlin

import common.Book
import common.Input
import common.Output
import common.ScoreCalculator


class ScoreCalculatorImpl : ScoreCalculator {

    override fun calculateResult(input: Input, output: Output): Long {
        var result: Long = 0

        var currentTime: Long = 0

        val solvedBooks = hashSetOf<Book>()

        output.entries.forEach {
            val library = it.library
            val books = it.scannedBooks

            currentTime += library.signup

            var booksTime = currentTime
            books
                .chunked(library.shippingRate)
                .forEach { booksThisDay ->
                    booksTime++

                    if (booksTime < input.days) {
                        booksThisDay.forEach { book ->
                            if (!solvedBooks.contains(book)) {
                                result += book.score
                            } else {
                                println("Warning: book ${book.id} was already scanned and we are trying to scan it again in library ${library.id}")
                            }
                        }
                    }
                }
        }

        return result
    }
}
