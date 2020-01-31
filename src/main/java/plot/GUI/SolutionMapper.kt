package plot.GUI

import common.HandledRide
import common.Input
import common.Output
import common.Reader
import common.score.kotlin.ScoreCalculatorImpl
import net.ericaro.surfaceplotter.Mapper
import java.io.File

class SolutionMapper : Mapper {

    private val solver = ScoreCalculatorImpl()
    private val input = createInput()

    override fun f1(p0: Float, p1: Float): Float {
        if (p0 <= 0 || p0 > 3) return 0f
        if (p1 <= 0 || p1 > 3) return 0f

        val rides = listOf(p0, p1)

        val re = rides
            .mapIndexed { index, value ->
                (index to value.toInt()) to (value - value.toInt().toFloat())
            }
            .filter { it.first.second != 0 }
            .groupBy { it.first.second }
            .flatMap { entry ->
                entry.value.sortedBy { it.second }
            }
            .map { pair ->
                HandledRide(pair.first.first, pair.first.second - 1)
            }

        val output = Output(
            handledRides = re
        )

        return solver.calculateResult(input, output).toFloat()
    }

    private fun createInput(): Input =
        File("inputs/a_example.in")
            .inputStream()
            .use {
                Reader.readInput(it)
            }

    override fun f2(p0: Float, p1: Float): Float {
        return 0f
    }
}