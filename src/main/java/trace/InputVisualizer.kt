package trace

import common.Input
import common.Output
import org.nield.kotlinstatistics.percentile
import java.io.File

fun writeInputVisualization(input: Input, output: Output, fileName: String) {
    val file = File(fileName)
    file.writeText("")
    val writer = file.writer()
    input
        .libraries
        .map { it.signup.toDouble() }
        .toPercentileDistribution()
        .map { it.x.toString() + "\t" + it.y.toString() }
        .forEach {
            writer.write(it + "\n")
        }
    writer.close()
}

class Point(
    val x: Double,
    val y: Double
)

fun List<Double>.toPercentileDistribution(): List<Point> = (1..100)
    .map {
        it to this.percentile(it.toDouble())
    }
    .map { (percentile, value) ->
        Point(
            x = percentile.toDouble(),
            y = if (value.isNaN()) 0.0 else value
        )
    }
