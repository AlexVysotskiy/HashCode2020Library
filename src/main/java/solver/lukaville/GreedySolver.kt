package solver.lukaville

import common.Input
import common.Output
import common.Solver

class GreedySolver(override val name: String = "lukaville.GreedySolver") : Solver {

    override fun solve(input: Input): Output {
        var total = 0
        val result = arrayListOf<Int>()
        input.slices.sorted().forEachIndexed { index, value ->
            if (total + value < input.max) {
                total += value
                result.add(index)
            }
        }
        return Output(result)
    }

}
