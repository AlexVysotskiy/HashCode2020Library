package common.helpers

import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.wrapped.ProgressBarWrappedIterator
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SwarmOptimizer(
    private val initialPosition: FloatArray,
    private val params: Params = Params(),
    private val calculateScore: (Array<FloatArray>, IntArray) -> Unit
) {
    @Volatile
    private var globalMaxValue: Int = 0

    fun solve(): FloatArray {
        val entryTime = System.currentTimeMillis()

        val particlePositions = Array(params.particleCount) {
            FloatArray(initialPosition.size) {
                val deviation = Random.nextFloat() * params.initialXSpread - params.initialXSpread / 2
                (initialPosition[it] + deviation).clipTo(params.minX, params.maxX)
            }
        }

        val outputScores = IntArray(params.particleCount)

        calculateScore(particlePositions, outputScores)

        val particles = Array(params.particleCount) {
            initParticle(particlePositions[it], outputScores[it])
        }
        val bestParticle = particles.maxBy { it.localMaxValue }!!

        globalMaxValue = bestParticle.localMaxValue
        val globalMax = bestParticle.localMax.copyOf()

        var inertia = params.initialInertia
        val executor = Executors.newFixedThreadPool(params.parallelism)
        println("Initialized swarm optimizer in ${System.currentTimeMillis() - entryTime} ms")

        var totalUpdate = 0L
        var totalCalculate = 0L
        val progressIterator = ProgressBar.wrap((0 until params.maxIterationCount).toList(), "").iterator() as ProgressBarWrappedIterator<Int>
        progressIterator.progressBar.apply {
            extraMessage = "Max: $globalMaxValue"
        }

        val chunkSize = particles.size / params.parallelism
        progressIterator.forEach {
            totalUpdate += measureTimeMillis {
                val latch = CountDownLatch(params.parallelism)
                (0 until params.parallelism).forEach {
                    executor.submit {
                        val start = it * chunkSize
                        val end = ((it + 1) * chunkSize)
                        (start until end).forEach { i ->
                            val particle = particles[i]
                            particle.velocity.forEachIndexed { index, velocity ->
                                val r0 = Random.nextFloat()
                                val r1 = Random.nextFloat()
                                val self = params.c0 * r0 * (particle.localMax[index] - particle.position[index])
                                val global = params.c1 * r1 * (globalMax[index] - particle.position[index])

                                particle.velocity[index] = inertia * velocity + self + global
                                particle.velocity[index] =
                                    particle.velocity[index].clipTo(-params.maxVelocity, params.maxVelocity)
                            }

                            particle.position.forEachIndexed { index, _ ->
                                particle.position[index] += particle.velocity[index]
                                particle.position[index] = particle.position[index].clipTo(params.minX, params.maxX)
                            }
                        }
                        latch.countDown()
                    }
                }
                latch.await()
            }

            totalCalculate += measureTimeMillis {
                particles.indices.forEach {
                    val particle = particles[it]
                    particlePositions[it] = particle.position
                }
            }

            totalCalculate += measureTimeMillis {
                calculateScore(particlePositions, outputScores)
            }

            totalCalculate += measureTimeMillis {
                particles.indices.forEach {
                    val particle = particles[it]
                    val outputScore = outputScores[it]
                    if (outputScores[it] > particle.localMaxValue) {
                        particle.localMaxValue = outputScore
                        particle.position.copyInto(particle.localMax)
                    }

                    if (outputScore > globalMaxValue) {
                        globalMaxValue = outputScore
                        particle.position.copyInto(globalMax)
                        progressIterator.progressBar.extraMessage = ("Max: $globalMaxValue")
                    }
                }
            }

            inertia *= params.inertiaDecrement
        }

        println("Spend on update $totalUpdate ms")
        println("Spend on score $totalCalculate ms")
        executor.shutdownNow()

        return globalMax
    }

    private fun initParticle(position: FloatArray, localMaxValue: Int): Particle {
        return Particle(
            localMax = position.copyOf(),
            localMaxValue = localMaxValue,
            position = position,
            velocity =  FloatArray(initialPosition.size) { Random.nextFloat() * params.maxVelocity }
        )
    }

    private fun Float.clipTo(min: Float, max: Float) =
        max(min, min(max, this))

    class Particle(
        val localMax: FloatArray,
        var localMaxValue: Int,
        val position: FloatArray,
        val velocity: FloatArray
    )

    class Params(
        val c0: Float = 1f,
        val c1: Float = 1f,

        val particleCount: Int = 10,
        val maxVelocity: Float = 4f,

        val minX: Float = 0f,
        val maxX: Float = 4f,

        val maxIterationCount: Int = 500,

        val initialInertia: Float = 1f,
        val inertiaDecrement: Float = 1f,

        val parallelism: Int = 8,
        val initialXSpread: Float = 2f
    )
}
