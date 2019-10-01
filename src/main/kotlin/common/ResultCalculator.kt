package common

import common.model.Orientation
import common.model.Photo
import common.model.Slide
import kotlin.math.min

class ResultCalculator {

    fun calculateResult(input: Input, output: Output): Long {
        val photos = input.photos
        checkDuplicates(output.slideshow)
        return output.slideshow.zipWithNext().sumBy { (first, second) ->
            checkCorrectness(photos, first)
            checkCorrectness(photos, second)
            calculateScore(photos, first, second)
        }.toLong()
    }

    private fun checkDuplicates(slideshow: Collection<Slide>) {
        val set = mutableSetOf<Int>()
        slideshow.forEach {
            if (!set.add(it.firstPhoto)) {
                println("Duplicate photo found: ${it.firstPhoto}")
                throw RuntimeException()
            }
            it.secondPhoto?.let { photoId ->
                if (!set.add(photoId)) {
                    println("Duplicate photo found: ${it.secondPhoto}")
                    throw RuntimeException()
                }
            }
        }
    }

    private fun checkCorrectness(photos: List<Photo>, slide: Slide) {
        val firstPhotoOrientation = photos[slide.firstPhoto].orientation
        val secondPhotoOrientation = slide.secondPhoto?.let {
            photos[it].orientation
        }
        if (firstPhotoOrientation == Orientation.HORIZONTAL &&
            secondPhotoOrientation != null
        ) {
            println("[!!!] Incorrect slide $slide: first photo is horizontal second should be null but it was ${slide.secondPhoto}")
            throw RuntimeException()
        }

        if (firstPhotoOrientation == Orientation.VERTICAL && secondPhotoOrientation != Orientation.VERTICAL) {
            println("[!!!] Incorrect slide $slide: both photos should be vertical but was: ${slide.firstPhoto}, ${slide.secondPhoto}")
            throw RuntimeException()
        }
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
