package solver.shikasd

import common.*

class GreedySolver(override val name: String = "shikasd.GreedySolver") : Solver {

    override fun solve(input: Input): Output {
        var tick = 0
        val carTakenUntil = IntArray(input.vehicles)
        val carPositions = Array(input.vehicles) { Point(0, 0) }
        val sortedRides = input.rides.mapIndexed { index, ride -> index to ride  }.toMutableList()

        val handledRides = mutableListOf<HandledRide>()
        while (tick < input.timeLimit) {
            carTakenUntil.forEachIndexed { index, time ->
                if (time > tick) return@forEachIndexed
                val carPosition = carPositions[index]

                val rides = sortedRides.asSequence().filter { (_, it) ->  it.endTime >= it.distance() + carPosition.distanceTo(it.start) + tick }
                val availableRide = rides.minBy { carPosition.distanceTo(it.second.start) } ?: return@forEachIndexed

                sortedRides.remove(availableRide)
                carTakenUntil[index] = tick + carPosition.distanceTo(availableRide.second.start) + availableRide.second.distance()
                handledRides.add(
                    HandledRide(availableRide.first, index)
                )
                carPositions[index] = availableRide.second.end
            }
            tick++
        }

        return Output(handledRides)
    }

}
