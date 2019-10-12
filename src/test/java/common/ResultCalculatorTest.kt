package common

import common.model.CompilationStep
import common.score.ResultCalculator
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Test
import java.io.File

class ResultCalculatorTest {

    @Test
    fun example() {
        val calculator = ResultCalculator()
        val input = createInput()
        val output = createOutput()

        val result = calculator.calculateResult(input, output)

        assertThat(result, IsEqual(60L))
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
                CompilationStep("c1", 1),
                CompilationStep("c0", 0),
                CompilationStep("c3", 1),
                CompilationStep("c2", 0),
                CompilationStep("c2", 1),
                CompilationStep("c4", 0),
                CompilationStep("c5", 1)
            )
        )
    }
}
