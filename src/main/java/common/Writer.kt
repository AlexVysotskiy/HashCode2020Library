package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        val groupedRides = output.handledRides.groupBy { it.vehicleIndex }
        groupedRides.forEach {
            writer.write(it.value.size.toString())
            writer.write(" ")
            it.value.forEach {
                writer.write(it.rideIndex.toString())
                writer.write(" ")
            }
            writer.write("\n")
        }
        writer.flush()
        writer.close()
    }

}
