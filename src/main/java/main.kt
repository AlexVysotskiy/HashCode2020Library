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
            "5742876508028928"
        ),
        InputFile(
            "inputs/b_read_on.txt",
            "5651204726063104"
        ),
        InputFile(
            "inputs/c_incunabula.txt",
            "5680006004998144"
        ),
        InputFile(
            "inputs/d_tough_choices.txt",
            "5654843402223616"
        ),
        InputFile(
            "inputs/e_so_many_books.txt",
            "5649463653695488"
        ),
        InputFile(
            "inputs/f_libraries_of_the_world.txt",
            "5649463653695488"
        )
    )

    solvers.forEach {
        executeSolver(inputFiles, it)
    }
}
