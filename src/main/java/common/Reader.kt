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

        val max = parseLong(stringTokenizer.nextToken())
        val typeCount = parseInt(stringTokenizer.nextToken())

        stringTokenizer = StringTokenizer(bufferReader.readLine())
        val types = ArrayList<Int>(typeCount)
        for (i in 0 until typeCount) {
            val slices = parseInt(stringTokenizer.nextToken())
            types.add(slices)
        }

        return Input(
            max,
            types
        )
    }
}
