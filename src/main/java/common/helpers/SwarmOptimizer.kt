package common.helpers

import me.tongfei.progressbar.ProgressBar
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SwarmOptimizer(
    private val initialPosition: FloatArray,
    private val params: Params = Params(),
    private val calculateScore: (FloatArray) -> Long
) {
    @Volatile
    private var globalMaxValue: Long = 0

    fun solve(): FloatArray {
        val entryTime = System.currentTimeMillis()

        val particles = Array(params.particleCount) { initParticle() }
        val bestParticle = particles.maxBy { it.localMaxValue }!!

        globalMaxValue = bestParticle.localMaxValue
        val globalMax = bestParticle.localMax.copyOf()

        var inertia = params.initialInertia
        val executor = Executors.newFixedThreadPool(params.parallelism)
        println("Initialized swarm optimizer in ${System.currentTimeMillis() - entryTime} ms")

        var totalUpdate = 0L
        var totalCalculate = 0L
        ProgressBar.wrap((0 until params.maxIterationCount).toList(), "Iterations").forEach {
            totalUpdate += measureTimeMillis {
                val latch = CountDownLatch(particles.size)
                particles.forEach { particle ->
                    executor.submit {
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
                        latch.countDown()
                    }
                }
                latch.await()
            }

            totalCalculate += measureTimeMillis {
                val latch = CountDownLatch(particles.size)
                particles.forEach { particle ->
                    executor.submit {
                        val score = calculateScore(particle.position)
                        if (score > particle.localMaxValue) {
                            particle.localMaxValue = score
                            particle.position.copyInto(particle.localMax)
                        }

                        if (score > globalMaxValue) {
                            globalMaxValue = score
                            particle.position.copyInto(globalMax)
                        }
                        latch.countDown()
                    }
                }
                latch.await()
            }

            inertia *= params.inertiaDecrement
        }

        println("Spend on update $totalUpdate ms")
        println("Spend on score $totalCalculate ms")
        executor.shutdownNow()

        return globalMax
    }

    private fun initParticle(): Particle {
        val xSpread = params.maxX - params.minX
        val position = FloatArray(initialPosition.size) {
            (initialPosition[it] + Random.nextFloat() * xSpread + params.minX).clipTo(params.minX, params.maxX)
        }
        return Particle(
            localMax = position.copyOf(),
            localMaxValue = calculateScore(position),
            position = position,
            velocity =  FloatArray(initialPosition.size) { Random.nextFloat() * params.maxVelocity }
        )
    }

    private fun Float.clipTo(min: Float, max: Float) =
        max(min, min(max, this))

    class Particle(
        val localMax: FloatArray,
        var localMaxValue: Long,
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

        val maxIterationCount: Int = 10_000,

        val initialInertia: Float = 1f,
        val inertiaDecrement: Float = 1f,

        val parallelism: Int = 8
    )
}
