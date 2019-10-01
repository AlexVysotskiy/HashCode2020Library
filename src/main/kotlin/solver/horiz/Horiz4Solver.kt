package solver.horiz

import com.vdurmont.etaprinter.ETAPrinter
import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import common.model.SlideWithPhotos
import solver.SlidesGenerator
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet
import kotlin.math.min

class Horiz4Solver(override val name: String = "horiz4") : Solver {

    override fun solve(input1: Input): Output {

        val slides = SlidesGenerator.generate(input1)

        val slideIdToSlide = slides.map { it.firstPhotoModel.id to it }.toMap()

        val photosWithSet = slides.toMutableList()
        val slidesSet = LinkedHashSet<Int>(slides.map { it.firstPhotoModel.id })

//        val tagsTop = PriorityQueue<Tag>(
//            Comparator<Tag> { o1, o2 ->
//                o1.count.compareTo(o2.count)
//            }
//        )

        val result = mutableListOf<Int>()

        val tagToPhotos = LinkedHashMap<String, MutableList<Int>>()

//        val avPhotos = photoSet.asSequence().map { it.tags.size }.average()

        slidesSet.forEach { slideId ->
            val slide = slideIdToSlide[slideId]!!
            slide.tags.forEach {
                val photos1 = tagToPhotos[it] ?: mutableListOf()
                photos1 += slideId
                tagToPhotos[it] = photos1
            }
        }

        val graph = LinkedHashMap<Int, MutableList<Int>>()

        tagToPhotos.forEach { t, u ->

            for (i in 0 until u.size) {
                for (j in i + 1 until u.size) {
                    graph[u[i]] = (graph[u[i]] ?: mutableListOf()).apply { add(u[j]) }
                    graph[u[j]] = (graph[u[j]] ?: mutableListOf()).apply { add(u[i]) }
                }
            }
        }

//        val etaPrinter = ETAPrinter.init(photoSet.size.toLong())

        while (!slidesSet.isEmpty()) {
            var thisPhoto = slidesSet.maxBy { graph[it]?.size ?: 0 }!!
            slidesSet.remove(thisPhoto)
//            etaPrinter.update(1)

            var nexts = graph[thisPhoto]?.filter { slidesSet.contains(it) }
            if (nexts?.isNotEmpty() == true) {
                result += thisPhoto
            }
            while (nexts?.isNotEmpty() == true) {
                val next =
                    nexts.maxBy { calculateScore(input1.photos, slideIdToSlide[thisPhoto]!!.toSlide(), slideIdToSlide[it]!!.toSlide()) }!!
                result += next
                val removed = slidesSet.remove(next)

//                etaPrinter.update(if (removed) 1 else 0)

                thisPhoto = next
                nexts = graph[thisPhoto]?.filter { slidesSet.contains(it) }
            }
        }


        return Output(result.map { slideIdToSlide[it]!!.toSlide() })
    }

    class Tag(
        val str: String,
        val count: Int
    )

    class PhotoWithSet(
        val id: Int,
        val tags: Set<String>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PhotoWithSet

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
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

    fun SlideWithPhotos.toSlide(): Slide =
        Slide(firstPhotoModel, secondPhotoModel)

}