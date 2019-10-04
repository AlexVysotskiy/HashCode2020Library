import common.*
import solver.other.OtherSolver
import uploader.Uploader
import uploader.optimizer.RandomOneOptimizer

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
        println(input)
        val output = solver.solve(input)

        val optimizer = RandomOneOptimizer()

        val newOutput = optimizer.optimize(input, output)

        val score = resultCalculator.calculateResult(input, newOutput)

        println("Score for $inputFile = $score")

        // Writer.write(output, File("$inputFile.out").outputStream())

        results.add(inp to output)

        score
    }.sum()

    println("> Total score for $name = $totalScore")

    uploader.upload(results)

    println()
    println()
}

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { OtherSolver() }
    )

    val inputFiles = listOf(
        InputFile("inputs/a_example.in", "0")
//        InputFile("inputs/b_lovely_landscapes.txt", "5239399268745216"),
//        InputFile("inputs/c_memorable_moments.txt", "5185683152961536")
//        InputFile("inputs/d_pet_pictures.txt", "6378347655331840")
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
