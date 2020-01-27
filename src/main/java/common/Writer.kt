package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        writer.write(output.types.size.toString())
        writer.newLine()

        output.types.forEach { type ->
            writer.write(type)
            writer.write(" ")
        }

        writer.flush()
        writer.close()
    }

}
