package common.score.python

import java.io.File
import java.util.concurrent.TimeUnit

class PythonScoreCalculator {

    fun calculateResult(input: File, output: File): Long {
        val inputFilePath = input.absolutePath
        val outputFilePath = output.absolutePath
        val workingDirectory = File("external_calculator")

        val score = "python3 score.py $inputFilePath $outputFilePath".runCommand(workingDirectory)
        return score.trim().toLongOrNull() ?: -1
    }

    private fun String.runCommand(workingDir: File): String {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    }
}
