package solver.lukaville

import common.*
import common.helpers.SwarmOptimizer
import kotlin.math.max
import kotlin.system.measureTimeMillis

class GpuGreedySolver(override val name: String = "lukaville.GreedySolver") : Solver {

    private val workItems = 2048
    private val greedyParamsCount = 3

    override fun solve(input: Input): Output {
        val solver = GpuSolver()

        val scoresOutput = IntArray(workItems)
        solver.initialize(input, workItems, greedyParamsCount, scoresOutput)

        println("Solver initialized")

        val optimizer = SwarmOptimizer(
            initialPosition = FloatArray(greedyParamsCount) { 1f },
            params = SwarmOptimizer.Params(
                c0 = 2f,
                c1 = 2f,
                initialInertia = 0.5f,
                particleCount = workItems,
                maxX = 10f,
                minX = 0.1f,
                maxIterationCount = 300,
                initialXSpread = 2f,
                parallelism = Runtime.getRuntime().availableProcessors()
            )
        ) { coeff: Array<FloatArray>, output: IntArray ->

            val time = measureTimeMillis {
                solver.solve(coeff, output)
            }

            println(time)
        }

        val bestOption = optimizer.solve()

        solver.terminate()

        return doGreedy(input, bestOption)
    }

    private fun doGreedy(input: Input, params: FloatArray): Output {
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

                    if (canFinishAt > ride.endTime) return@maxBy -10e8f

                    val bonus = if (canStartAt <= ride.startTime) input.bonus else 0
                    val actualStart = max(canStartAt, ride.startTime)
                    val cost = ride.distance + bonus
                    val distanceToStart = carPosition.distanceTo(ride.start)
                    val waitingTime = actualStart - canStartAt
                    val score = cost * params[0] - distanceToStart * params[1] - waitingTime * params[2]
                    score
                } ?: return@forEachIndexed

                val (rideIndex, ride) = nextRide
                sortedRides.remove(nextRide)
                carTakenUntil[index] =
                    max(tick + carPosition.distanceTo(ride.start), nextRide.second.startTime) + ride.distance
                handledRides.add(
                    HandledRide(rideIndex, index)
                )
                carPositions[index] = ride.end
            }
            tick++
        }

        return Output(handledRides)
    }
}
