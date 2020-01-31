package common

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.util.*

object Reader {

    @JvmStatic
    fun readInput(file: InputStream): Input {
        val bufferReader = BufferedReader(InputStreamReader(file))
        var stringTokenizer = StringTokenizer(bufferReader.readLine())

        val gridSize = Size(stringTokenizer.parseInt(), stringTokenizer.parseInt())
        val carCount = stringTokenizer.parseInt()
        val rideCount = stringTokenizer.parseInt()
        val bonus = stringTokenizer.parseInt()
        val timeLimit = stringTokenizer.parseInt()

        val rides = mutableListOf<Ride>()
        for (i in 0 until rideCount) {
            stringTokenizer = StringTokenizer(bufferReader.readLine())

            val start = Point(stringTokenizer.parseInt(), stringTokenizer.parseInt())
            val finish = Point(stringTokenizer.parseInt(), stringTokenizer.parseInt())
            val startTime = stringTokenizer.parseInt()
            val finishTime = stringTokenizer.parseInt()
            rides.add(
                Ride(
                    start = start,
                    end = finish,
                    startTime = startTime,
                    endTime = finishTime
                )
            )
        }

        return Input(
            gridSize = gridSize,
            vehicles = carCount,
            rides = rides,
            bonus = bonus,
            timeLimit = timeLimit
        )
    }
}

private fun StringTokenizer.parseInt() = parseInt(nextToken())
