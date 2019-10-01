package solver.greedy

import common.Input
import common.Output
import common.Solver
import common.model.Orientation
import common.model.Photo
import common.model.Slide
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

class GreedySolver(override val name: String = "greedy") : Solver {

    private val allPhotos: MutableList<Photo> = ArrayList()

    private val uniqTags = HashSet<String>()
    private var averageTagPerPhoto: Long = 0

    private val goodPhotosVertical = sortedTreeSet()
    private val goodPhotosHoriz = sortedTreeSet()

    private fun sortedTreeSet() = ArrayList<Pair<Photo, Int>>()

    val droppedPhotos = ArrayList<Photo>()

    override fun solve(input: Input): Output {

        allPhotos.addAll(input.photos)

        collectStats()

        prepareData()
        goodPhotosVertical.sortBy { it.second }
        goodPhotosHoriz.sortBy { it.second }

        val rawSlides = ArrayList<Slide>()

        rawSlides.addAll(makeVerticalSlides(goodPhotosVertical.map { it.first }))

        val slides = rawSlides

        slides.addAll(makeHorizSlides(goodPhotosHoriz.map { it.first }))

        droppedPhotos.shuffle()
        droppedPhotos.forEach {
            slides.add(Slide(it, null))
        }

        return Output(slides)
    }

    private fun makeVerticalSlides(goodPhotosVertical: Collection<Photo>): List<Slide> {
//        val rawSlides = ArrayList<Slide>()
//
//        var a = if (iterator.hasNext()) iterator.next() else null
//        while (iterator.hasNext() && a != null) {
//            val b = iterator.next()
//
//            if (a.getScoreWithRight(b) > 0) {
//                rawSlides.add(Slide(a, b))
//                a = if (iterator.hasNext()) iterator.next() else null
//            } else {
//                a = b
//                //droppedPhotos.add(a)
//            }
//        }
//
//        return rawSlides

        val rawSlides = ArrayList<Slide>()
        goodPhotosVertical.chunked(2).forEach { lst ->
            val l = lst[0]
            val r = lst.getOrNull(1)

            rawSlides.add(Slide(l, r))
        }

        return rawSlides
    }

    private fun makeHorizSlides(goodPhotosHor: Collection<Photo>): List<Slide> {
        val rawSlides = ArrayList<Slide>()
        goodPhotosHor.chunked(2).forEach { lst ->
            val l = lst[0]
            val r = lst.getOrNull(1)

            if (l.getScoreWithRight(r) > 0) {
                rawSlides.add(Slide(l, null))
                rawSlides.add(Slide(r!!, null))
            } else {
                droppedPhotos.add(l)
                if (r != null) {
                    droppedPhotos.add(r)
                }
            }
        }

        return rawSlides
    }

    private fun collectStats() {
        var totalNonUniqTags = 1L
        allPhotos.forEach { photo ->
            totalNonUniqTags += photo.tags.size
            uniqTags.addAll(photo.tags)
        }

        averageTagPerPhoto = totalNonUniqTags / allPhotos.size
    }

    private fun prepareData() {
        allPhotos.forEach { photo ->

            val value = photo.tags.intersect(uniqTags).size
            if (photo.isHoriz()) {
                goodPhotosHoriz.add(photo to value)
            } else {
                goodPhotosVertical.add(photo to value)
            }
        }
    }

    private fun Slide.getScoreWithRight(b: Slide?): Int {
        if (b == null) return 0 else {
            return minOf(
                (this.tags().intersect(b.tags())).size,
                (this.tags().minus(b.tags())).size,
                (b.tags().minus(this.tags())).size
            )
        }
    }

    private fun Slide.tags() = this.getPhoto1().tags.plus(this.getPhoto2()?.tags ?: emptyList())

    private fun Slide.getPhoto1() = allPhotos.find { it.id == this.firstPhoto }!!
    private fun Slide.getPhoto2() = allPhotos.find { it.id == this.secondPhoto }

    private fun Photo.getScoreWithRight(b: Photo?): Int {
        if (b == null) return 0 else {
            return minOf(
                (this.tags.intersect(b.tags)).size,
                (this.tags.minus(b.tags)).size,
                (b.tags.minus(this.tags)).size
            )
        }
    }

    private fun Photo.isHoriz() = this.orientation == Orientation.HORIZONTAL
}