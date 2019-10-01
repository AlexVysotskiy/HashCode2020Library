package solver.bruteforce

import com.vdurmont.etaprinter.ETAPrinter
import common.Input
import common.Output
import common.ResultCalculator
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import java.util.*
import kotlin.math.min

class RandomSolverOptimized(override val name: String = "random_optimized") : Solver {

    private val random = Random()
    private val scoreCalculator = ResultCalculator()

    private val cycles = 5

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

            val remaining = slides.toMutableList()
            val result = mutableListOf(remaining.last()).also {
                remaining.removeAt(remaining.size - 1)
            }

            val etaPrinter = ETAPrinter.init(remaining.size.toLong())

            while (remaining.isNotEmpty()) {
                val currentSlide = result.last()

                val nextSlide = remaining.subList(0, min(remaining.size, 2000))
                    .map { remainingCandidate ->
                        calculateScore(input.photos, currentSlide, remainingCandidate) to remainingCandidate
                    }
                    .maxBy { it.first }?.second

                nextSlide!!.let { slideToWrite ->
                    result.add(slideToWrite)
                    remaining.remove(slideToWrite)
                    try {
                        etaPrinter.update(1)
                    } catch (exception: Exception) {
                    }
                }
            }

            val output = Output(result)
            val score = scoreCalculator.calculateResult(input, output)

            score to output
        }.maxBy { it.first }

        return max!!.second
    }

    private fun calculateScore(photos: List<Photo>, slide1: Slide, slide2: Slide): Int {
        val firstTags = getTagsForSlide(photos, slide1)
        val secondTags = getTagsForSlide(photos, slide2)

        val commonTagsCount = firstTags.intersect(secondTags).size
        val commonFirstNotSecond = firstTags.minus(secondTags).size
        val commonSecondNotFirst = secondTags.minus(firstTags).size

        return min(min(commonTagsCount, commonFirstNotSecond), commonSecondNotFirst)
    }

    private fun getTagsForSlide(photos: List<Photo>, slide: Slide): Set<String> {
        val firstPhotoTags = photos[slide.firstPhoto].tags
        val secondPhotoTags = slide.secondPhoto?.let { photos[it] }?.tags ?: emptyList()
        return (firstPhotoTags + secondPhotoTags).toSet()
    }
}
