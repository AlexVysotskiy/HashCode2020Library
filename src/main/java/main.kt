import common.InputFile
import common.Solver
import solver.shikasd.ShikaSdSolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { ShikaSdSolver() }
    )

    val inputFiles = listOf(
//        InputFile(
//            "inputs/a_example.txt",
//            "5691439912583168"
//        ),
//        InputFile(
//            "inputs/b_read_on.txt",
//            "6588393567813632"
//        ),
//        InputFile(
//            "inputs/c_incunabula.txt",
//            "5367802785431552"
//        ),
//        InputFile(
//            "inputs/d_tough_choices.txt",
//            "6688877079166976"
//        ),
        InputFile(
            "inputs/e_so_many_books.txt",
            "5804289507196928"
        )
//        InputFile(
//            "inputs/f_libraries_of_the_world.txt",
//            "4595015879753728"
//        )
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
