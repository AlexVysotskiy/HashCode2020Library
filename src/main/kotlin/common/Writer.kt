package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        writer.write(output.slideshow.size.toString())
        writer.newLine()

        output.slideshow.forEach { slide ->
            writer.write(slide.firstPhoto.toString())
            if (slide.secondPhoto != null) {
                writer.write(" ")
                writer.write(slide.secondPhoto.toString())
            }
            writer.newLine()
        }

        writer.flush()
        writer.close()
    }

}