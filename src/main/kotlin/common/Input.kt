package common

import common.model.Photo

class Input(
    val photos: List<Photo>
) {
    fun stat() {
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
        sumTags = (sumTags.toFloat() / photos.size).toInt()
        println("max tags: $maxCount\nmin tags: $minCount\naverage tags: $sumTags")
        println("uniq tags:${map.size}")
        map.toList().sortedBy { -it.second }.forEach { println("tag: ${it.first} count ${it.second}") }
    }
}
