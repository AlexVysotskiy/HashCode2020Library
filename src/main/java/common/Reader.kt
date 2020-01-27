package common

import common.model.FileNode
import common.model.TargetValue
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Long.parseLong
import java.util.*

object Reader {

    @JvmStatic
    fun readInput(file: InputStream): Input {
        val bufferReader = BufferedReader(InputStreamReader(file))
        val stringTokenizer = StringTokenizer(bufferReader.readLine())

        val max = parseLong(stringTokenizer.nextToken())
        val typeCount = parseLong(stringTokenizer.nextToken())
        val result = arrayOf

        val stringTokenizer = StringTokenizer(bufferReader.readLine())
        for (i in 0 until typeCount) {
            val slices = parseInt(stringTokenizer.nextToken())

        }

        return Input(
            max,

        )
    }
}
