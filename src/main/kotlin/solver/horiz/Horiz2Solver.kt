package solver.horiz

import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Slide
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

class Horiz2Solver(override val name: String = "horiz") : Solver {

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

        val graph = LinkedHashMap<Int, Int?>()

        tagToPhotos.forEach { t, u ->
            if (u.size == 2) {
                graph[u[0]] = u[1]
                graph[u[1]] = u[0]
            }
        }

        while (!photoSet.isEmpty()) {
            val lastPhoto = photoSet.last()
            photoSet.remove(lastPhoto)

            var next = graph[lastPhoto.id]?.takeIf { photoSet.contains(PhotoWithSet(it, emptySet())) }
            if (next != null) {
                result += lastPhoto.id
            }
            while (next != null) {
                result += next
                photoSet.remove(PhotoWithSet(next, emptySet()))
                next = graph[next]?.takeIf { photoSet.contains(PhotoWithSet(it, emptySet())) }
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

}