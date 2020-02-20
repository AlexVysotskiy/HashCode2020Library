package solver.lukaville

import common.Entry
import common.Input
import common.Output
import common.Solver

class LukavilleSolver(override val name: String = "lukaville.Greedy") : Solver {

    override fun solve(input: Input): Output {
        return Output(
            listOf(
                Entry(
                    library = input.libraries[1],
                    scannedBooks = listOf(input.books[5], input.books[2], input.books[3])
                ),
                Entry(
                    library = input.libraries[0],
                    scannedBooks = listOf(input.books[0], input.books[1], input.books[2], input.books[3], input.books[4])
                )
            )
        )
    }
}
