import common.InputFile
import common.Solver
import solver.shikasd.GreedySolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { GreedySolver() }
    )

    val inputFiles = listOf(
//        InputFile(
//            "inputs/a_example.in",
//            "5742876508028928"
//        ),
//        InputFile(
//            "inputs/b_should_be_easy.in",
//            "5651204726063104"
//        )
//        InputFile(
//            "inputs/c_no_hurry.in",
//            "5680006004998144"
//        )
        InputFile(
            "inputs/d_metropolis.in",
            "5654843402223616"
        )
//        InputFile(
//            "inputs/e_high_bonus.in",
//            "5649463653695488"
//        )
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
