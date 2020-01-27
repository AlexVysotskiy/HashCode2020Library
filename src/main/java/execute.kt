import common.*
import common.score.kotlin.KotlinScoreCalculator
import uploader.Uploader
import java.io.File

fun executeSolver(inputs: List<InputFile>, solverFactory: () -> Solver) {
    val name = solverFactory().name
    val uploader = Uploader()

    println("Checking solution [$name]")

    val resultCalculator = KotlinScoreCalculator()

    val results = mutableListOf<Pair<InputFile, Output>>()

    val totalScore = inputs.map { inp ->
        val solver = solverFactory()

        val inputFile = inp.toFile()
        val input = Reader.readInput(inputFile.inputStream())

        val output = solver.solve(input)
        val outputFile = File("$inputFile.out")
        Writer.write(output, outputFile.outputStream())

        val score = resultCalculator.calculateResult(input, output)

        resultCalculator.writeTrace("$inputFile.trace.json")

        println("Score for $inputFile = $score (max sum goals = ${input.targets.values.sumBy { it.goal.toInt() }})")

        results.add(inp to output)

        score
    }.sum()

    println("> Total score for $name = $totalScore")

    uploader.upload(results)

    println()
    println()
}
