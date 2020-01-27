package solver.lukaville

import common.*
import common.score.kotlin.KotlinScoreCalculator
import java.io.File
import kotlin.random.Random

class GreedySolver2(override val name: String = "lukaville.GreedySolver2") : Solver {

    override fun solve(input: Input): Output {
        val scoreCalculator = KotlinScoreCalculator()
        var total = 0
        val result = arrayListOf<Int>()

        input.slices.sorted().forEachIndexed { index, value ->
            if (total + value < input.max) {
                total += value
                result.add(index)
            }
        }

        var maxResult: Pair<Long, Output> = scoreCalculator.calculateResult(input, Output(result)) to Output(result)
        println(maxResult.first)
        while (true) {
            val newSlices = input.slices.toMutableList()
            result.clear()
            total = 0

            val dropElementsSize = Random.nextInt(300)
            repeat(dropElementsSize) {
                newSlices.removeAt(Random.nextInt(newSlices.size))
            }

            newSlices.sorted().forEachIndexed { index, value ->
                if (total + value < input.max) {
                    total += value
                    result.add(index)
                }
            }

            val output = Output(result)
            val score = scoreCalculator.calculateResult(input, output)
            if (maxResult.first < score) {
                maxResult = score to Output(result)
                println(score)
                Writer.write(output, File("inputs/max.out").outputStream())
            }
        }
        return Output(result)
    }

}
