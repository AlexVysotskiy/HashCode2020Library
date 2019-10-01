package common.model

data class Slide(
    val firstPhoto: Int,
    val secondPhoto: Int? = null
) {

    constructor(firstPhotoModel: Photo, secondPhotoModel: Photo?) : this(
        firstPhoto = firstPhotoModel.id,
        secondPhoto = secondPhotoModel?.id
    )

}


data class SlideWithPhotos(
    val firstPhotoModel: Photo,
    val secondPhotoModel: Photo? = null
) {
    val tags = mutableSetOf<String>().apply {
        addAll(firstPhotoModel.tags)
        if (secondPhotoModel != null) {
            addAll(secondPhotoModel.tags)
        }
    }
}