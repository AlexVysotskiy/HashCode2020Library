package solver.other

import common.Input
import common.Output
import common.Solver

class OtherSolver(override val name: String = "other") : Solver {

    override fun solve(input: Input): Output {
        Thread.sleep(1000)
        return Output(emptyList())
    }

}
