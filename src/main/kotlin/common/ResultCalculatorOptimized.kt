package common

import kotlin.math.min

class ResultCalculatorOptimized {

//    private val scores = hashMapOf<Pair<Slide, Slide>, Int>()
//
//    fun calculateResult(input: Input, output: Output): Long {
//        val photos = input.photos
//        return output.slideshow
//            .asSequence()
//            .zipWithNext { first, second -> calculateScore(photos, first, second) }
//            .sum()
//            .toLong()
//    }
//
//    private fun calculateScore(photos: List<Photo>, slide1: Slide, slide2: Slide): Int {
//        val key = Pair(slide1, slide2)
//        if (scores[key] != null) {
//            return scores[key]!!
//        }
//
//        val firstTags = getTagsForSlide(photos, slide1)
//        val secondTags = getTagsForSlide(photos, slide2)
//
//        val commonTagsCount = firstTags.intersect(secondTags).size
//        val commonFirstNotSecond = firstTags.minus(secondTags).size
//        val commonSecondNotFirst = secondTags.minus(firstTags).size
//
//        val value = min(min(commonTagsCount, commonFirstNotSecond), commonSecondNotFirst)
//        scores[key] = value
//        return value
//    }
//
//    private fun getTagsForSlide(photos: List<Photo>, slide: Slide): Set<String> {
//        val firstPhotoTags = photos[slide.firstPhoto].tags
//        val secondPhotoTags = slide.secondPhoto?.let { photos[it] }?.tags ?: emptyList()
//        return (firstPhotoTags + secondPhotoTags).toSet()
//    }
}
