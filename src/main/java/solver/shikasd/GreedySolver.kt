package solver.shikasd

import common.*
import common.helpers.SwarmOptimizer
import common.score.kotlin.ScoreCalculatorImpl
import common.score.kotlin.calculateScoreFast
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

        val initialPositions = FloatArray(input.rides.size)
        val scoreCalculator = ScoreCalculatorImpl()
        val optimizer = SwarmOptimizer(
            initialPositions,
            params = SwarmOptimizer.Params(
                c0 = 0.5f,
                c1 = 0.5f,
                particleCount = 1000,
                maxX = (input.vehicles + 1).toFloat() - 0.001f,
                maxIterationCount = 30
            )
        ) {
            calculateScoreFast(input, it).toLong()
        }

        return optimizer.solve().toOutput()
    }

    fun Output.toFloatArray(input: Input): FloatArray {
        val carFractions = FloatArray(input.vehicles) { it + 1.001f }
        val initialPositions = FloatArray(input.rides.size)
        handledRides.forEach {
            initialPositions[it.rideIndex] = carFractions[it.vehicleIndex]
            carFractions[it.vehicleIndex] += 0.001f
        }
        return initialPositions
    }

    fun ScoreCalculator.calculateResult(input: Input, floatArray: FloatArray): Long =
        calculateResult(input, floatArray.toOutput())

    fun FloatArray.toOutput(): Output {
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
