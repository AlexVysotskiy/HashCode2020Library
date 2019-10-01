package solver.horiz

import com.vdurmont.etaprinter.ETAPrinter
import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet
import kotlin.math.min

class Horiz3Solver(override val name: String = "horiz") : Solver {

    override fun solve(input: Input): Output {

        val photosWithSet = input.photos.map { PhotoWithSet(it.id, it.tags.toSet()) }.toMutableList()
        val photoSet = LinkedHashSet(photosWithSet)

//        val tagsTop = PriorityQueue<Tag>(
//            Comparator<Tag> { o1, o2 ->
//                o1.count.compareTo(o2.count)
//            }
//        )

        val result = mutableListOf<Int>()

        val tagToPhotos = LinkedHashMap<String, MutableList<Int>>()

        val avPhotos = photoSet.asSequence().map { it.tags.size }.average()

        photoSet.forEach { photo ->
            photo.tags.forEach {
                val photos1 = tagToPhotos[it] ?: mutableListOf()
                photos1 += photo.id
                tagToPhotos[it] = photos1
            }
        }

        val graph = LinkedHashMap<Int, MutableList<Int>>()

        tagToPhotos.forEach { t, u ->
            if (u.size == 2) {
                graph[u[0]] = (graph[u[0]] ?: mutableListOf()).apply { add(u[1]) }
                graph[u[1]] = (graph[u[1]] ?: mutableListOf()).apply { add(u[0]) }
            }
        }

        val etaPrinter = ETAPrinter.init(photoSet.size.toLong())

        while (!photoSet.isEmpty()) {
            var thisPhoto = photoSet.maxBy { graph[it.id]?.size ?: 0 }!!
            photoSet.remove(thisPhoto)
            etaPrinter.update(1)

            var nexts = graph[thisPhoto.id]?.filter { photoSet.contains(PhotoWithSet(it, emptySet())) }
            if (nexts?.isNotEmpty() == true) {
                result += thisPhoto.id
            }
            while (nexts?.isNotEmpty() == true) {
                val next = nexts.maxBy { calculateScore(input.photos, Slide(thisPhoto.id), Slide(it)) }!!
                result += next
                val removed = photoSet.remove(PhotoWithSet(next, emptySet()))

                etaPrinter.update(if (removed) 1 else 0)

                thisPhoto = PhotoWithSet(next, emptySet())
                nexts = graph[thisPhoto.id]?.filter { photoSet.contains(PhotoWithSet(it, emptySet())) }
            }
        }


        return Output(result.map { Slide(it) })
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

}