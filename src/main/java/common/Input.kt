package common

import kotlin.math.abs

data class Input(
    val gridSize: Size,
    val vehicles: Int,
    val rides: List<Ride>,
    val bonus: Int,
    val timeLimit: Int
)

data class Ride(
    val start: Point,
    val end: Point,
    val startTime: Int,
    val endTime: Int
) {
    val distance: Int = abs(start.x - end.x) + abs(start.y - end.y)
}

data class Point(
    val x: Int,
    val y: Int
) {
    fun distanceTo(other: Point) = abs(x -  other.x) + abs(y - other.y)
}

data class Size(
    val width: Int,
    val height: Int
)
