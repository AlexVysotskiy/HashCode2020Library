package common

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Integer.parseInt
import java.util.*

object Reader {

    @JvmStatic
    fun readInput(file: InputStream): Input {
        val bufferReader = BufferedReader(InputStreamReader(file))
        var stringTokenizer = StringTokenizer(bufferReader.readLine())

        val booksNum = stringTokenizer.parseInt()
        val libraryNum = stringTokenizer.parseInt()
        val daysTotal = stringTokenizer.parseInt()

        val books = mutableListOf<Book>()
        stringTokenizer = StringTokenizer(bufferReader.readLine())
        (0 until booksNum).forEach {
            books.add(
                Book(
                    id = it,
                    score = stringTokenizer.parseInt()
                )
            )
        }

        val libraries = mutableListOf<Library>()
        (0 until libraryNum).forEach {
            stringTokenizer = StringTokenizer(bufferReader.readLine())
            val booksNum = stringTokenizer.parseInt()
            val signUp = stringTokenizer.parseInt()
            val shippingRate = stringTokenizer.parseInt()
            stringTokenizer = StringTokenizer(bufferReader.readLine())
            libraries.add(
                Library(
                    id = it,
                    signup = signUp,
                    shippingRate = shippingRate,
                    books = (0 until booksNum).map {
                        books[stringTokenizer.parseInt()]
                    }
                )
            )
        }

        return Input(
            days = daysTotal,
            books = books,
            libraries = libraries
        )
    }
}

private fun StringTokenizer.parseInt() = parseInt(nextToken())
