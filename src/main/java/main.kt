import common.InputFile
import common.Solver
import solver.narrow.NarrowSolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { NarrowSolver() }
//        { UrgentSolver() }
    )

    val inputFiles = listOf(
//        InputFile("inputs/a_example.in", "0"),
        InputFile("inputs/b_narrow.in", "0")
//        InputFile("inputs/c_urgent.in", "0"),
//        InputFile("inputs/d_typical.in", "0"),
//        InputFile("inputs/e_intriguing.in", "0"),
//        InputFile("inputs/f_big.in", "0")
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
