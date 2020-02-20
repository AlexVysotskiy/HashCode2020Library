package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        writer.write(output.entries.size.toString())
        writer.newLine()

        output.entries.forEach {
            writer.write(it.library.id.toString())
            writer.write(" ")
            writer.write(it.scannedBooks.size.toString())
            writer.newLine()

            it.scannedBooks.forEach {
                writer.write(it.id.toString())
                writer.write(" ")
            }

            writer.newLine()
        }

        writer.flush()
        writer.close()
    }

}
