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
        val output = createOutput(input)

        val result = calculator.calculateResult(input, output)

        assertThat(result, IsEqual(16L))
    }

    private fun createInput(): Input =
        File("inputs/a_example.txt")
            .inputStream()
            .use {
                Reader.readInput(it)
            }

    private fun createOutput(input: Input): Output {
        return Output(
            listOf(
                Entry(input.libraries[1], scannedBooks = listOf(input.books[5], input.books[2], input.books[3])),
                Entry(
                    input.libraries[0], scannedBooks = listOf(
                        input.books[0],
                        input.books[1],
                        input.books[2],
                        input.books[3],
                        input.books[4]
                    )
                )
            )
        )
    }
}
