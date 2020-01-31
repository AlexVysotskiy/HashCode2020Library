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
    fun distance(): Int = abs(start.x -  end.x) + abs(start.y - end.y)
}

data class Point(
    val x: Int,
    val y: Int
)

data class Size(
    val width: Int,
    val height: Int
)
