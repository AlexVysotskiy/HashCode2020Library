package uploader.optimizer

import com.vdurmont.etaprinter.ETAPrinter
import common.Input
import common.Output
import common.ResultCalculatorOptimized
import java.lang.Math.min
import java.util.*

class RandomOneOptimizer {

    private val random = Random()
    private val calculator = ResultCalculatorOptimized()

    private class ToSwap(
        val first: Int,
        val second: Int
    )

    private val cycles = 100

    fun optimize(input: Input, orOutput: Output): Output {
        val mutableSlides = orOutput.slideshow.toMutableList()
        val newOutput = Output(mutableSlides)

        println("Optimizing...")

        val etaPrinter = ETAPrinter.init(cycles.toLong())

        val initialResult = calculator.calculateResult(input, newOutput)

        (0 until cycles).forEach {
            val slideToMove = random.nextInt(mutableSlides.size)
            val theBestSwap = (0 until mutableSlides.size).map { currIndex ->
                mutableSlides.swap(currIndex, slideToMove)
                val result = calculator.calculateResult(input, newOutput)
                mutableSlides.swap(slideToMove, currIndex)
                result to ToSwap(currIndex, slideToMove)
            }.maxBy { it.first }!!.second

            mutableSlides.swap(theBestSwap.first, theBestSwap.second)

            try {
                etaPrinter.update(1)
            } catch (exception: Exception) {
            }
        }

        val afterResult = calculator.calculateResult(input, newOutput)

        println()
        println("OPTIMIZATION: from $initialResult to $afterResult (+${afterResult - initialResult})")

        return newOutput
    }

    private fun <T> MutableList<T>.swap(indexOne: Int, indexTwo: Int) {
        val one = get(indexOne)
        val second = get(indexTwo)
        set(indexOne, second)
        set(indexTwo, one)
    }

}