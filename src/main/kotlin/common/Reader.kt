package common

import common.model.Photo
import common.model.fromString
import java.io.InputStream
import java.util.*

object Reader {

    fun readInput(stream: InputStream): Input {
        val reader = Scanner(stream)

        val photosCount = reader.nextInt()

        reader.nextLine()

        val photos = (0 until photosCount).map {
            val photoDescriptionInfo = reader.nextLine().split(' ')
            val orientationString = photoDescriptionInfo[0]
            val photo = Photo(
                id = it,
                orientation = fromString(orientationString),
                tags = photoDescriptionInfo.drop(2)
            )
            photo
        }

        return Input(photos)
    }
}
