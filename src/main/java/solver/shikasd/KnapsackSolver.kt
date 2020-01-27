package solver.shikasd

import common.Input
import common.Output
import common.Solver
import common.helpers.CostValue
import common.helpers.Knapsack2
import utils.Knapsack

class KnapsackSolver(override val name: String = "shikasd.Knapsack"): Solver {

    override fun solve(input: Input): Output {
        val solution = Knapsack.solve(input.max.toInt(), input.slices) { CostValue(it.toLong(), it.toLong()) }
        return Output(solution.map { input.slices.indexOf(it) })
    }
}
