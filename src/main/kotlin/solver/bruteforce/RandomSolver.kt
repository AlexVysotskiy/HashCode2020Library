package solver.bruteforce

import common.Input
import common.Output
import common.ResultCalculator
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import java.util.*

class RandomSolver(override val name: String = "bruteforce") : Solver {

    private val random = Random()
    private val scoreCalculator = ResultCalculator()

    private val cycles = 2

    override fun solve(input: Input): Output {
        val horizontalPhotos = input.photos.filter { it.orientation == Orientation.HORIZONTAL }
        val verticalPhotos = input.photos.filter { it.orientation == Orientation.VERTICAL }

        val max = (0 until cycles).map {
            val oddVertical = verticalPhotos
                .shuffled(random)
                .chunked(2)
                .mapNotNull {
                    if (it.size == 2) {
                        Pair(it[0], it[1])
                    } else {
                        null
                    }
                }

            val slides = (horizontalPhotos + oddVertical).shuffled(random).map {
                if (it is Pair<*, *>) {
                    Slide(it.first as Photo, it.second as Photo)
                } else {
                    Slide(it as Photo, null)
                }
            }

            val output = Output(slides)
            val score = scoreCalculator.calculateResult(input, output)

            score to output
        }.maxBy { it.first }



        return max!!.second
    }

}
