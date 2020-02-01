package solver.shikasd

import common.*
import common.helpers.SwarmOptimizer
import common.score.kotlin.ScoreCalculatorImpl
import kotlin.random.Random

class GreedySolver(override val name: String = "shikasd.GreedySolver") : Solver {

    override fun solve(input: Input): Output {
//        var tick = 0
//        val carTakenUntil = IntArray(input.vehicles)
//        val carPositions = Array(input.vehicles) { Point(0, 0) }
//        val sortedRides = input.rides.mapIndexed { index, ride -> index to ride  }.toMutableList()
//
//        val handledRides = mutableListOf<HandledRide>()
//        while (tick < input.timeLimit) {
//            carTakenUntil.forEachIndexed { index, time ->
//                if (time > tick) return@forEachIndexed
//                val carPosition = carPositions[index]
//
//                val rides = sortedRides.asSequence().filter { (_, it) -> it.endTime >= it.distance() + carPosition.distanceTo(it.start) + tick }
//                val availableRide = rides.minBy { carPosition.distanceTo(it.second.start) } ?: return@forEachIndexed
//
//                sortedRides.remove(availableRide)
//                carTakenUntil[index] = tick + carPosition.distanceTo(availableRide.second.start) + availableRide.second.distance()
//                handledRides.add(
//                    HandledRide(availableRide.first, index)
//                )
//                carPositions[index] = availableRide.second.end
//            }
//            sortedRides.removeAll { it.second.endTime <= tick }
//            tick++
//        }
//
//

        val initialPositions = FloatArray(input.rides.size)
        val scoreCalculator = ScoreCalculatorImpl()
        val optimizer = SwarmOptimizer(
            initialPositions,
            params = SwarmOptimizer.Params(
                c0 = 0.4f,
                c1 = 0.6f,
                particleCount = input.rides.size * 2,
                maxX = (input.vehicles + 1).toFloat()
            )
        ) {
            scoreCalculator.calculateResult(input, it)
        }

        return optimizer.solve().toOutput()
    }

    private fun ScoreCalculator.calculateResult(input: Input, floatArray: FloatArray): Long =
        calculateResult(input, floatArray.toOutput())

    private fun FloatArray.toOutput(): Output {
        val result = zip(indices)
            .filter { it.first >= 1 }
            .sortedBy { it.first }
            .map { (value, rideIndex) ->
                val carNumber = value.toInt() - 1
                HandledRide(rideIndex, carNumber)
            }
        return Output(result)
    }

}
