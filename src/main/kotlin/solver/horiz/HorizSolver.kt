package solver.horiz

import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Slide
import java.util.*
import kotlin.collections.HashMap

class HorizSolver(override val name: String = "horiz") : Solver {

    override fun solve(input: Input): Output {

        val photosWithSet = input.photos.map { PhotoWithSet(it.id, it.tags.toSet()) }
        val photoSet = photosWithSet.toHashSet()

        val tagsTop = PriorityQueue<Tag>(
            Comparator<Tag> { o1, o2 ->
                o1.count.compareTo(o2.count)
            }
        )

        val result = mutableListOf<PhotoWithSet>()
        while (true) {
            val tagCount = LinkedHashMap<String, Int>()

            val avPhotos = photoSet.asSequence().map { it.tags.size }.average()

            photoSet.forEach {
                it.tags.forEach {
                    tagCount[it] = (tagCount[it] ?: 0) + 1
                }
            }

            tagCount.forEach { t, u ->
                tagsTop.add(Tag(t, u))
                if (tagsTop.size > avPhotos)
                    tagsTop.poll()
            }

            val currentTopTags = tagsTop.map { it.str }
            val newResult = photoSet.filter { it.tags.containsAll(currentTopTags) }.sortedBy { -it.tags.size }

            if (newResult.isEmpty()) break

            result += newResult
            newResult.forEach {
                photoSet.remove(PhotoWithSet(it.id, emptySet()))
            }
        }

        result += photoSet.sortedBy { -it.tags.size }

        return Output(result.map { Slide(it.id) })
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