package solver

import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import common.model.SlideWithPhotos
import kotlin.math.min

const val DIFF_TAGS = 15

class ArtemSolver(override val name: String = "artem") : Solver {

    override fun solve(input: Input): Output {
        return Output(SlidesGenerator.generate(input).map { Slide(it.firstPhotoModel, it.secondPhotoModel) })
    }

}

object SlidesGenerator {

    fun generate(input: Input): List<SlideWithPhotos> {


        val result = mutableListOf<SlideWithPhotos>()

        val verticalPhotos = mutableListOf<Photo>()

        input.photos.forEach {
            if (it.orientation == Orientation.VERTICAL) {
                verticalPhotos.add(it)
            } else {
                result.add(SlideWithPhotos(it))
            }
        }
        result.addAll(handleVerticalPhotos(verticalPhotos))
        return result
    }

    private fun handleVerticalPhotos(photos: List<Photo>): MutableList<SlideWithPhotos> {
        var minCount = Integer.MAX_VALUE
        var maxCount = Integer.MIN_VALUE
        var sumTags = 0
        val map: MutableMap<String, Int> = mutableMapOf()

        for (slide in photos) {
            val slideSize = slide.tags.size
            minCount = Math.min(minCount, slide.tags.size)
            maxCount = Math.max(maxCount, slideSize)
            sumTags += slideSize
            for (tag in slide.tags) {
                if (map.containsKey(tag)) {
                    val currentValue: Int = map[tag]!!
                    map[tag] = currentValue + 1
                } else {
                    map[tag] = 0
                }
            }
        }

        val sumWeight = map.toList().sumBy { it.second }
        val normalized: MutableMap<String, Double> = mutableMapOf()
        map.forEach { normalized[it.key] = it.value.toDouble() / sumWeight }

        val normalizedPhotos =
            photos.map {
                TempopraryPhoto(
                    photo = it,
                    weight = it.tags.sumByDouble { tag -> normalized[tag]!! }
                )
            }.sortedBy { -it.weight }

        val takedPhotos = mutableSetOf<Int>()
        val slides = mutableListOf<SlideWithPhotos>()


        for (photo in normalizedPhotos) {
            if (takedPhotos.contains(photo.photo.id)) {
                continue
            }
            takedPhotos.add(photo.photo.id)
            val phot2 = takePhotoFor(photo, normalizedPhotos, takedPhotos, minCount)
            if (phot2 != null) {
                slides.add(SlideWithPhotos(photo.photo, phot2))
            }

        }

        return slides
    }

    private fun takePhotoFor(
        photo: SlidesGenerator.TempopraryPhoto,
        normalizedPhotos: List<SlidesGenerator.TempopraryPhoto>,
        takedPhotos: MutableSet<Int>,
        minDiff: Int
    ): Photo? {
        var bestPhoto: Photo? = null
        for (newPhoto in normalizedPhotos) {
            if (takedPhotos.contains(newPhoto.photo.id)) {
                continue
            }

            val firstTags = photo.photo.tags
            val secondTags = newPhoto.photo.tags
            val commonFirstNotSecond = firstTags.minus(secondTags).size
            val commonSecondNotFirst = secondTags.minus(firstTags).size
            val diff = commonFirstNotSecond + commonSecondNotFirst

            if (diff >= minDiff) {
                bestPhoto = newPhoto.photo
                break
            }
        }
        if (bestPhoto == null) {
            for (newPhoto in normalizedPhotos) {
                if (!takedPhotos.contains(newPhoto.photo.id)) {
                    bestPhoto = newPhoto.photo
                    break
                }
            }

        }
        bestPhoto?.id?.let {
            takedPhotos.add(it)
        }
        return bestPhoto
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


    class TempopraryPhoto(
        val photo: Photo,
        val weight: Double
    )

}