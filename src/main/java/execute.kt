import common.*
import uploader.Uploader

fun executeSolver(inputs: List<InputFile>, solverFactory: () -> Solver) {
    val name = solverFactory().name
    val uploader = Uploader()

    println("Checking solution [$name]")

    val resultCalculator = ResultCalculator()

    val results = mutableListOf<Pair<InputFile, Output>>()

    val totalScore = inputs.map { inp ->
        val solver = solverFactory()

        val inputFile = inp.toFile()
        val input = Reader.readInput(inputFile.inputStream())

        val output = solver.solve(input)
        val score = resultCalculator.calculateResult(input, output)

        println("Score for $inputFile = $score")

        // Writer.write(output, File("$inputFile.out").outputStream())

        results.add(inp to output)

        score
    }.sum()

    println("> Total score for $name = $totalScore")

    // uploader.upload(results)

    println()
    println()
}