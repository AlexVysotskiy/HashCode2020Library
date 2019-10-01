package common.model

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

fun fromString(string: String): Orientation = when (string) {
    "V" -> Orientation.VERTICAL
    else -> Orientation.HORIZONTAL
}