package solver.shikasd

import common.*
import common.helpers.SwarmOptimizer
import common.score.kotlin.ScoreCalculatorImpl
import kotlin.math.max
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class GreedySolver(override val name: String = "shikasd.GreedySolver") : Solver {

    @ExperimentalTime
    override fun solve(input: Input): Output {
        val scoreCalculator = ScoreCalculatorImpl()

        val optimizer = SwarmOptimizer(
            initialPosition = FloatArray(3) { 1f },
            params = SwarmOptimizer.Params(
                c0 = 2f,
                c1 = 2f,
                initialInertia = 0.5f,
                particleCount = 20,
                maxX = 10f,
                minX = 0.1f,
                maxIterationCount = 100,
                initialXSpread = 2f,
                parallelism = Runtime.getRuntime().availableProcessors()
            )
        ) {
            val time = measureTimedValue {
                val output = doGreedy(input, it)
                scoreCalculator.calculateResult(input, output)
            }

//            println(time.duration.inMilliseconds)

            time.value
        }

        return optimizer.solve().toOutput()
    }

    fun doGreedy(input: Input, params: FloatArray): Output {
        var tick = 0
        val carTakenUntil = IntArray(input.vehicles)
        val carPositions = Array(input.vehicles) { Point(0, 0) }
        val sortedRides = input.rides.mapIndexed { index, ride -> index to ride }.toMutableList()

        val handledRides = mutableListOf<HandledRide>()
        while (tick < input.timeLimit) {
            carTakenUntil.forEachIndexed { index, time ->
                if (time > tick) return@forEachIndexed
                val carPosition = carPositions[index]

                val nextRide = sortedRides.maxBy { (index, ride) ->
                    val canStartAt = tick + carPosition.distanceTo(ride.start)
                    val canFinishAt = canStartAt + ride.distance

                    if (canFinishAt > ride.endTime) return@maxBy 0f

                    val bonus = if (canStartAt <= ride.startTime) input.bonus else 0
                    val actualStart = max(canStartAt, ride.startTime)
                    val cost = ride.distance + bonus
                    val distanceToStart = carPosition.distanceTo(ride.start)
                    val waitingTime = actualStart - canStartAt
                    val score = cost * params[0] - distanceToStart * params[1] - waitingTime * params[2]
                    score
                } ?: return@forEachIndexed

                sortedRides.remove(nextRide)
                carTakenUntil[index] = max(tick + carPosition.distanceTo(nextRide.second.start), nextRide.second.startTime) + nextRide.second.distance
                handledRides.add(
                    HandledRide(nextRide.first, index)
                )
                carPositions[index] = nextRide.second.end
            }
            tick++
        }
        return Output(handledRides)
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
