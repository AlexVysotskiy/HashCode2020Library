package common.score.kotlin

import common.Input
import common.Output
import common.ScoreCalculator

class KotlinScoreCalculator : ScoreCalculator {

    override fun calculateResult(input: Input, output: Output): Long =
        output.types.sumByLong { input.slices[it].toLong() }

    private inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
        var sum: Long = 0
        for (element in this) {
            sum += selector(element)
        }
        return sum
    }
}
