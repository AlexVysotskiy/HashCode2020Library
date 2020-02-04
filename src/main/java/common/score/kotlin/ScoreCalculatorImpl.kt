package common.score.kotlin

import common.Input
import common.Output
import common.ScoreCalculator
import java.lang.IllegalArgumentException
import kotlin.math.abs


class ScoreCalculatorImpl : ScoreCalculator {

    override fun calculateResult(input: Input, output: Output): Long {
        val cars = output.handledRides.groupBy { it.vehicleIndex }

        val visitedRides = hashSetOf<Int>()
        var score = 0L

        cars.forEach { (_, rides) ->
            var carTime = 0
            var carX = 0
            var carY = 0

            rides.forEach {
                val ride = input.rides[it.rideIndex]
                if (it.rideIndex in visitedRides) throw IllegalArgumentException("Ride ${it.rideIndex} was already handled")
                visitedRides.add(it.rideIndex)

                val spentTimeMovingToStart = abs(carX - ride.start.x) + abs(carY - ride.start.y)

                carTime += spentTimeMovingToStart

                if (carTime < ride.startTime) {
                    // we need to wait for the start time
                    carTime = ride.startTime
                }
                val startArrivalTime = carTime

                val spentTimeMovingToFinish = abs(ride.start.x - ride.end.x) + abs(ride.start.y - ride.end.y)
                carTime += spentTimeMovingToFinish
                val finishArrivalTime = carTime

                carX = ride.end.x
                carY = ride.end.y

                // arrived on time, woohoo
                if (finishArrivalTime <= ride.endTime) {
                    score += ride.distance

                    if (startArrivalTime == ride.startTime) {
                        // bonus point because we start precisely on time
                        score += input.bonus
                    }
                }
            }
        }

        return score
    }
}
