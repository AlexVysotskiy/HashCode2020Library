package solver.shikasd

import common.*

class GreedySolver(override val name: String = "shikasd.GreedySolver") : Solver {

    override fun solve(input: Input): Output {
        var tick = 0
        val carTakenUntil = IntArray(input.vehicles)
        val carPositions = Array(input.vehicles) { Point(0, 0) }
        val sortedRides = input.rides.toMutableList()

        val handledRides = mutableListOf<HandledRide>()
        while (tick < input.timeLimit) {
            carTakenUntil.forEachIndexed { index, time ->
                if (time > tick) return@forEachIndexed
                val carPosition = carPositions[index]

                val rides = sortedRides.asSequence().filter { it.endTime >= it.distance() + carPosition.distanceTo(it.start) + tick }
                val availableRide = rides.minBy { carPosition.distanceTo(it.start) } ?: return@forEachIndexed

                sortedRides.remove(availableRide)
                carTakenUntil[index] = tick + carPosition.distanceTo(availableRide.start) + availableRide.distance()
                handledRides.add(
                    HandledRide(input.rides.indexOf(availableRide), index)
                )
            }
            tick++
        }

        return Output(handledRides)
    }

}
