package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        writer.write(output.compilationSteps.size.toString())
        writer.newLine()

        output.compilationSteps.forEach { compilationStep ->
            writer.write(compilationStep.name)
            writer.write(" ")
            writer.write(compilationStep.serverIndex.toString())
            writer.newLine()
        }

        writer.flush()
        writer.close()
    }

}
