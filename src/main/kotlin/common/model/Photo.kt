package common.model

class Photo(
    val id: Int,
    val orientation: Orientation,
    val tags: List<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    fun diff(photo: Photo) {

    }

}
