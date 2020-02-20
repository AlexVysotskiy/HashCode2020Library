package trace

import java.util.concurrent.TimeUnit
import common.Book
import common.Input
import common.Library
import common.Output
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

fun writeOutputVisualization(input: Input, output: Output, fileName: String) {
    val traceEvents = mutableListOf<TraceEvent>()

    var currentTime: Long = 0

    val solvedBooks = hashSetOf<Book>()
    val solvedLibraries = hashSetOf<Library>()

    output.entries.forEachIndexed { libraryNumber, entry ->
        val library = entry.library
        val books = entry.scannedBooks

        solvedLibraries += library
        currentTime += library.signup

        traceEvents += TraceEvent(
            pid = libraryNumber.toLong(),
            tid = 0,
            ph = Phase.DurationStart,
            name = library.id.toString() + "_signup",
            ts = (currentTime - library.signup).toDouble(),
            cname = "startup",
            cat = "signup"
        )
        traceEvents += TraceEvent(
            pid = libraryNumber.toLong(),
            tid = 0,
            ph = Phase.DurationEnd,
            name = library.id.toString() + "_signup",
            ts = currentTime.toDouble()
        )

        var booksTime = currentTime
        books
            .chunked(library.shippingRate)
            .forEachIndexed { index, booksThisDay ->
                booksTime++

                var scored = booksTime <= input.days
                booksThisDay.forEach { book ->
                    scored = scored && !solvedBooks.contains(book)
                    traceEvents += TraceEvent(
                        pid = libraryNumber.toLong(),
                        tid = index.toLong(),
                        ph = Phase.DurationStart,
                        name = book.id.toString(),
                        ts = booksTime.toDouble() - 1,
                        cname = if (scored) "good" else "bad",
                        cat = if (scored) "scored" else "not_scored",
                        args = mapOf(
                            "score" to book.score.toString(),
                            "library_id" to library.id.toString()
                        )
                    )
                    traceEvents += TraceEvent(
                        pid = libraryNumber.toLong(),
                        tid = index.toLong(),
                        ph = Phase.DurationEnd,
                        name = book.id.toString(),
                        ts = booksTime.toDouble(),
                        cname = if (scored) "good" else "bad"
                    )
                }
            }

        val traceRoot = TraceRoot(
            traceEvents = traceEvents,
            displayTimeUnit = TimeUnit.MILLISECONDS
        )

        val json = Json(JsonConfiguration.Default)
        File(fileName).writeText(json.stringify(TraceRoot.serializer(), traceRoot))
    }
}