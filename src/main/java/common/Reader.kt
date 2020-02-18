package common

import java.io.InputStream
import java.lang.Integer.parseInt
import java.util.*

object Reader {

    @JvmStatic
    fun readInput(file: InputStream): Input {
        return Input()
    }
}

private fun StringTokenizer.parseInt() = parseInt(nextToken())
