package common

import common.score.kotlin.ScoreCalculatorImpl
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

    private fun createInput(): Input =
        File("inputs/a_example.in")
            .inputStream()
            .use {
                Reader.readInput(it)
            }

    private fun createOutput(): Output {
        return Output(
            listOf(
                HandledRide(0, 1),
                HandledRide(2, 2),
                HandledRide(1, 2)
            )
        )
    }
}
