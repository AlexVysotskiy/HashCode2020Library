package common.helpers

import com.google.ortools.algorithms.KnapsackSolver


class Knapsack2 {

    companion object {

        init {
            System.loadLibrary("jniortools");
        }

        @JvmStatic
        fun <T> solve(volume: Int, items: List<T>, mapper: (T) -> CostValue): List<T> {
            val solver = KnapsackSolver(
                com.google.ortools.algorithms.KnapsackSolver.SolverType.KNAPSACK_MULTIDIMENSION_BRANCH_AND_BOUND_SOLVER,
                "test"
            )

            val costValues = items.map(mapper)
            val profits = costValues.map { it.cost }.toLongArray()
            val weights: Array<LongArray> = arrayOf(costValues.map { it.value }.toLongArray())
            solver.init(profits, weights, longArrayOf(volume.toLong()))
            val solution: MutableList<T> = mutableListOf()
            for (i in 0 until items.size) {
                if (solver.bestSolutionContains(i)) {
                    solution.add(items[i])
                }
            }
            return solution

        }
    }
}