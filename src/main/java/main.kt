import common.InputFile
import common.Solver
import solver.lukaville.LukavilleSolver

fun main(args: Array<String>) {

    val solvers = listOf<() -> Solver>(
        { LukavilleSolver() }
    )

    val inputFiles = listOf(
        InputFile(
            "inputs/a_example.txt",
            "4653688085807104"
        ),
        InputFile(
            "inputs/b_read_on.txt",
            "5801757154213888"
        ),
        InputFile(
            "inputs/c_incunabula.txt",
            "5901915321794560"
        ),
        InputFile(
            "inputs/d_tough_choices.txt",
            "5056706962784256"
        ),
        InputFile(
            "inputs/e_so_many_books.txt",
            "6542451846152192"
        ),
        InputFile(
            "inputs/f_libraries_of_the_world.txt",
            "6288955762802688"
        )
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
