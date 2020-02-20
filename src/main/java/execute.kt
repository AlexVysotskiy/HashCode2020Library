import common.*
import common.score.kotlin.ScoreCalculatorImpl
import trace.writeOutputVisualization
import uploader.Uploader
import java.io.File

fun executeSolver(inputs: List<InputFile>, solverFactory: () -> Solver) {
    val name = solverFactory().name
    val uploader = Uploader()

    println("Checking solution [$name]")

    val resultCalculator = ScoreCalculatorImpl()

    val results = mutableListOf<Pair<InputFile, Output>>()
    val writeTrace = false

    val totalScore = inputs.map { inp ->
        val solver = solverFactory()

        val inputFile = inp.toFile()
        val input = Reader.readInput(inputFile.inputStream())

        val output = solver.solve(input)
        val outputFile = File("$inputFile.out")
        Writer.write(output, outputFile.outputStream())

        val score = resultCalculator.calculateResult(input, output)

        if (writeTrace) {
            writeOutputVisualization(input, output, "$inputFile.out.trace")
        }

        // writeInputVisualization(input, output, "$inputFile.input.viz.csv")

        println("Score for $inputFile = $score")

        results.add(inp to output)

        score
    }.sum()

    println("> Total score for $name = $totalScore")

    uploader.upload(results)

    println()
    println()
}
