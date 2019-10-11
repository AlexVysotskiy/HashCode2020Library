import common.InputFile
import common.Solver
import solver.narrow.NarrowSolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { NarrowSolver() }
    )

    val inputFiles = listOf(
        InputFile("inputs/a_example.in", "0")
//        InputFile("inputs/b_lovely_landscapes.txt", "5239399268745216"),
//        InputFile("inputs/c_urgent.txt", "0")
//        InputFile("inputs/d_pet_pictures.txt", "6378347655331840")
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
