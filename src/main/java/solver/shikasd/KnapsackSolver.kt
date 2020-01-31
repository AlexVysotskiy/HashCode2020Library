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
        val existing = arrayListOf<Int>()
        return Output(solution.map {
            var index = input.slices.indexOf(it)
            while (existing.contains(index)) {
                val newIndex = input.slices.subList(index + 1, input.slices.size).indexOf(it) + index + 1
                index = newIndex
            }
            existing.add(index)
            index
        })
    }
}
