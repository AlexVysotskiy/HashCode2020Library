package solver.narrow

import common.Input
import common.Output
import common.Solver
import common.model.CompilationStep

class NarrowSolver(override val name: String = "Narrow") : Solver {

    override fun solve(input: Input): Output {
        val compilationSteps = arrayListOf<CompilationStep>()

        

        return Output(compilationSteps)
    }

}
