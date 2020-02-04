package solver.lukaville

import common.Input
import common.Output
import common.Solver

class GpuGreedySolver(override val name: String = "lukaville.GreedySolver") : Solver {

    private val workItems = 1024
    private val greedyParamsCount = 3

    override fun solve(input: Input): Output {
        val solver = GpuSolver()

        val scoresOutput = IntArray(workItems)
        solver.initialize(input, workItems, greedyParamsCount, scoresOutput)

        val params = Array(workItems) {
            FloatArray(3) { 1.5f }
        }

        solver.solve(params, scoresOutput)

        scoresOutput.forEach {
            println("Computed score: $it")
        }

        solver.terminate()

        return Output(emptyList())
    }

}
