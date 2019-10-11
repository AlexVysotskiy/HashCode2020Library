package solver.narrow

import common.Input
import common.Output
import common.Solver
import common.model.CompilationStep

class NarrowSolver(override val name: String = "Narrow") : Solver {

    override fun solve(input: Input): Output {
        val compilationSteps = arrayListOf<CompilationStep>()

        val serverCount = input.servers

        input.nodes
            .forEach { (name, node) ->
                for (i in 0 until serverCount) {
                    if (node.dependencies.size <= 1) {
                        compilationSteps.add(CompilationStep(name, i))
                    }
                }
            }

        return Output(compilationSteps)
    }

}
