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
            initialPosition = FloatArray(5) { 1f },
            params = SwarmOptimizer.Params(
                c0 = 2f,
                c1 = 2f,
                initialInertia = 0.5f,
                particleCount = 16,
                maxX = 10f,
                minX = 0.1f,
                maxIterationCount = 300,
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

        return doGreedy(input, optimizer.solve())
    }

    fun doGreedy(input: Input, params: FloatArray): Output {
        var tick = 0
        val carTakenUntil = IntArray(input.vehicles)
        val carPositions = Array(input.vehicles) { Point(0, 0) }
        val rideTaken = BooleanArray(input.rides.size)

        val handledRides = mutableListOf<HandledRide>()
        while (tick < input.timeLimit) {
            carTakenUntil.indices.forEach { carIndex ->
                val time = carTakenUntil[carIndex]
                if (time > tick) return@forEach
                val carPosition = carPositions[carIndex]

                var maxIndex = -1
                var firstValue = true
                var maxValue = 0f
                input.rides.forEachIndexed { index, ride ->
                    if (rideTaken[index]) return@forEachIndexed

                    val canStartAt = tick + carPosition.distanceTo(ride.start)
                    val actualStart = max(canStartAt, ride.startTime)
                    val canFinishAt = actualStart + ride.distance

                    var score = 0f
                    if (canFinishAt <= ride.endTime) {
                        val distance =
                            if ((ride.end.x > 5000 || ride.end.y > 5000) && tick < input.timeLimit * params[4]) {
                                1000
                            } else {
                                0
                            }

                        val bonus = if (canStartAt <= ride.startTime) input.bonus else 0

                        val cost = ride.distance + bonus
                        val distanceToStart = carPosition.distanceTo(ride.start)
                        val waitingTime = actualStart - canStartAt
                        score = cost * params[0] - distanceToStart * params[1] - waitingTime * params[2] - distance * params[3]
                    }

                    if (firstValue || score > maxValue) {
                        maxValue = score
                        maxIndex = index
                        firstValue = false
                    }
                }

                if (maxIndex == -1) {
                    return@forEach
                }

                val ride = input.rides[maxIndex]
                rideTaken[maxIndex] = true
                carTakenUntil[carIndex] = max(tick + carPosition.distanceTo(ride.start), ride.startTime) + ride.distance
                handledRides.add(
                    HandledRide(maxIndex, carIndex)
                )
                carPositions[carIndex] = ride.end
            }
            val nextTick = carTakenUntil.min() ?: tick + 1
            if (nextTick <= tick) {
                tick ++
            } else {
                tick = nextTick
            }
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
