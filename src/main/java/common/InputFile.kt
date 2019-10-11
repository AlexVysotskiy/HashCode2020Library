package common

import java.io.File

class InputFile(
    val fileName: String,
    val dataSetId: String
) {

    fun toFile() = File(fileName)

}
