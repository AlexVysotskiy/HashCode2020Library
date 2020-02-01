import common.*
import common.score.kotlin.ScoreCalculatorImpl
import uploader.Uploader
import java.io.File

fun executeOptimizer(input: Input, output: Output, optimizerFactory: () -> Optimizer) {
    val name = optimizerFactory().name
    val uploader = Uploader()

    println("Optimizing solution using [$name]")

    val resultCalculator = ScoreCalculatorImpl()

    val results = mutableListOf<Pair<InputFile, Output>>()

//    val totalScore = inputs.map { inp ->
//        val solver = solverFactory()
//
//        val inputFile = inp.toFile()
//        val input = Reader.readInput(inputFile.inputStream())
//
//        val output = solver.solve(input)
//        val outputFile = File("$inputFile.out")
//        Writer.write(output, outputFile.outputStream())
//
//        val score = resultCalculator.calculateResult(input, output)
//
//        println("Score for $inputFile = $score")
//
//        results.add(inp to output)
//
//        score
//    }.sum()
//
//    println("> Total score for $name = $totalScore")
//
//    uploader.upload(results)
//
//    println()
//    println()
}
