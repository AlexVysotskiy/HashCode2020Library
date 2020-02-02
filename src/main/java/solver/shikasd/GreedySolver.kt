package solver.shikasd

import common.*
import common.helpers.SwarmOptimizer
import common.score.kotlin.ScoreCalculatorImpl
import common.score.kotlin.calculateScoreFast
import kotlin.math.max

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

                val nextRide = sortedRides.maxBy { (index, ride) ->
                    val canStartAt = tick + carPosition.distanceTo(ride.start)
                    val canFinishAt = canStartAt + ride.distance()

                    if (canFinishAt > ride.endTime) return@maxBy 0.0

                    val bonus = if (canStartAt <= ride.startTime) input.bonus else 0
                    val actualStart = max(canStartAt, ride.startTime)
                    val cost = ride.distance() + bonus
                    val distanceToStart = carPosition.distanceTo(ride.start)
                    val waitingTime = actualStart - canStartAt
                    val score = cost - distanceToStart * 1.5 - waitingTime * 2.0
                    score
                } ?: return@forEachIndexed

                sortedRides.remove(nextRide)
                carTakenUntil[index] = max(tick + carPosition.distanceTo(nextRide.second.start), nextRide.second.startTime) + nextRide.second.distance()
                handledRides.add(
                    HandledRide(nextRide.first, index)
                )
                carPositions[index] = nextRide.second.end
            }
            sortedRides.removeAll { it.second.endTime <= tick }
            tick++
        }

        val scoreCalculator = ScoreCalculatorImpl()
        val greedyScore = scoreCalculator.calculateResult(input, Output(handledRides))
        println("Greedy score: $greedyScore")
        val greedySolution = Output(handledRides).toFloatArray(input)

        val optimizer = SwarmOptimizer(
            greedySolution,
            params = SwarmOptimizer.Params(
                c0 = 1.5f,
                c1 = 2f,
                initialInertia = 0.5f,
                particleCount = 10000,
                maxX = (input.vehicles + 1).toFloat() - 0.001f,
                maxIterationCount = 1000,
                initialXSpread = 4f,
                parallelism = Runtime.getRuntime().availableProcessors()
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
