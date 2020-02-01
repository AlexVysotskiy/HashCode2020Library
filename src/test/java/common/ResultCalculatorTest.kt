package common

import common.score.kotlin.ScoreCalculatorImpl
import common.score.kotlin.calculateScoreFast
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test
import java.io.File

class ResultCalculatorTest {

    @Test
    fun example() {
        val calculator = ScoreCalculatorImpl()
        val input = createInput()
        val output = createOutput()

        val result = calculator.calculateResult(input, output)

        assertThat(result, IsEqual(10L))
    }

    @Test
    fun exampleFast() {
        val input = createInput()
        val output = createOutput()

        val result = calculateScoreFast(input, output.toFloatArray(input))

        assertThat(result, IsEqual(10))
    }

    private fun Output.toFloatArray(input: Input): FloatArray {
        val carFractions = FloatArray(input.vehicles) { it + 1.001f }
        val initialPositions = FloatArray(input.rides.size)
        handledRides.forEach {
            initialPositions[it.rideIndex] = carFractions[it.vehicleIndex]
            carFractions[it.vehicleIndex] += 0.001f
        }
        return initialPositions
    }

    private fun createInput(): Input =
        File("inputs/a_example.in")
            .inputStream()
            .use {
                Reader.readInput(it)
            }

    private fun createOutput(): Output {
        return Output(
            listOf(
                HandledRide(0, 0),
                HandledRide(2, 1),
                HandledRide(1, 1)
            )
        )
    }
}
