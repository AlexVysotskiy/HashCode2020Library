import common.InputFile
import common.Solver
import solver.lukaville.GreedySolver
import solver.shikasd.KnapsackSolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { GreedySolver() },
        { KnapsackSolver() }
    )

    val inputFiles = listOf(
        InputFile(
            "inputs/a_example.in",
            "5742876508028928"
        ),
        InputFile(
            "inputs/b_small.in",
            "5651204726063104"
        ),
        InputFile(
            "inputs/c_medium.in",
            "5680006004998144"
        ),
        InputFile(
            "inputs/d_quite_big.in",
            "5654843402223616"
        ),
        InputFile(
            "inputs/e_also_big.in",
            "5649463653695488"
        )
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
