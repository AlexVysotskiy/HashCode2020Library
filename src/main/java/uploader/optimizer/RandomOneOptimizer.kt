package uploader.optimizer

import common.Input
import common.Output
import java.util.*

class RandomOneOptimizer {

    private val random = Random()

    private class ToSwap(
        val first: Int,
        val second: Int
    )

    private val cycles = 100

    fun optimize(input: Input, orOutput: Output): Output {
//
//        println("Optimizing...")
//
//        val etaPrinter = ETAPrinter.init(cycles.toLong())
//
//        val initialResult = calculator.calculateResult(input, newOutput)
//
//        (0 until cycles).forEach {
//            val slideToMove = random.nextInt(mutableSlides.size)
//            val theBestSwap = (0 until mutableSlides.size).map { currIndex ->
//                mutableSlides.swap(currIndex, slideToMove)
//                val score = calculator.calculateResult(input, newOutput)
//                mutableSlides.swap(slideToMove, currIndex)
//                score to ToSwap(currIndex, slideToMove)
//            }.maxBy { it.first }!!.second
//
//            mutableSlides.swap(theBestSwap.first, theBestSwap.second)
//
//            try {
//                etaPrinter.update(1)
//            } catch (exception: Exception) {
//            }
//        }
//
//        val afterResult = calculator.calculateResult(input, newOutput)
//
//        println()
//        println("OPTIMIZATION: from $initialResult to $afterResult (+${afterResult - initialResult})")

        return Output(emptyList())
    }

    private fun <T> MutableList<T>.swap(indexOne: Int, indexTwo: Int) {
        val one = get(indexOne)
        val second = get(indexTwo)
        set(indexOne, second)
        set(indexTwo, one)
    }

}
