package common.score.kotlin

import common.Input
import common.helpers.sort.QuickSort
import kotlin.math.abs

fun calculateScoreFast(input: Input, array: FloatArray): Int {
    // todo: use object pools?

    val rides = array.copyOf()
    val indices = ShortArray(array.size) { it.toShort() }

    // sort rides by float part and at the same time sort indices in the same way
    QuickSort.quickSortArrayBasedOnSecond(indices, rides)

    val carTime = IntArray(input.vehicles)
    val carX = IntArray(input.vehicles)
    val carY = IntArray(input.vehicles)
    var totalScore = 0

    for (i in indices.indices) {
        val encodedRide = rides[i]
        val rideIndex = indices[i]
        val ride = input.rides[rideIndex.toInt()]

        val carIndex = encodedRide.toInt() - 1
        if (carIndex == -1) continue // this ride is skipped (assigned car encoded as 0)

        val spentTimeMovingToStart = abs(carX[carIndex] - ride.start.x) + abs(carY[carIndex] - ride.start.y)

        carTime[carIndex] += spentTimeMovingToStart

        if (carTime[carIndex] < ride.startTime) {
            // we need to wait for the start time
            carTime[carIndex] = ride.startTime
        }
        val startArrivalTime = carTime[carIndex]

        val spentTimeMovingToFinish = abs(ride.start.x - ride.end.x) + abs(ride.start.y - ride.end.y)
        carTime[carIndex] += spentTimeMovingToFinish
        val finishArrivalTime = carTime[carIndex]

        carX[carIndex] = ride.end.x
        carY[carIndex] = ride.end.y

        // arrived on time, woohoo
        if (finishArrivalTime <= ride.endTime) {
            totalScore += ride.distance()

            if (startArrivalTime == ride.startTime) {
                // bonus point because we start precisely on time
                totalScore += input.bonus
            }
        }
    }

    return totalScore
}
