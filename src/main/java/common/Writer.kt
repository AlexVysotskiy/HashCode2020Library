package common

import java.io.OutputStream

object Writer {

    fun write(output: Output, outputStream: OutputStream) {
        val writer = outputStream.bufferedWriter()

        val groupedRides = output.handledRides.groupBy { it.vehicleIndex }
        groupedRides.forEach {
            writer.write(it.value.size)
            writer.write(" ")
            it.value.forEach {
                writer.write(it.rideIndex)
                writer.write(" ")
            }
            writer.write("\n")
        }
    }

}
