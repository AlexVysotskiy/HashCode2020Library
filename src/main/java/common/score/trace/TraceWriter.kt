package common.score.trace

import common.model.TargetValue
import common.score.ComputationNode
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify
import trace.Phase
import trace.TraceEvent
import trace.TraceRoot
import java.io.File
import java.util.concurrent.TimeUnit


@UseExperimental(ImplicitReflectionSerializer::class, UnstableDefault::class)
fun writeChromeTrace(computationNodes: List<ComputationNode>, targets: List<TargetValue>, path: String) {
    val json = Json(JsonConfiguration.Default)

    val targetNames = targets.map { it.name }.toHashSet()
    val traceEvents = mutableListOf<TraceEvent>()

    var idCounter = 0L

    computationNodes.forEach { node ->
        node.traceEvents.forEach {
            val isTarget = it.fileNode.name in targetNames
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                ph = Phase.DurationStart,
                name = it.fileNode.name,
                ts = it.compilationBegin.toDouble(),
                cname = if (isTarget) "bad" else "good",
                cat = if (isTarget) "target" else "node",
                args = mapOf(
                    "replicationTime" to it.fileNode.replication.toString(),
                    "compilationTime" to it.fileNode.compilation.toString(),
                    "isTarget" to isTarget.toString()
                )
            )
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                ph = Phase.DurationEnd,
                name = it.fileNode.name,
                ts = it.compilationEnd.toDouble()
            )

            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                id = idCounter,
                ph = Phase.AsyncStart,
                name = "replication",
                ts = it.compilationEnd.toDouble(),
                args = mapOf(
                    "fileName" to it.fileNode.name
                )
            )
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                id = idCounter,
                ph = Phase.AsyncEnd,
                name = "replication",
                ts = it.replicationEnd.toDouble()
            )

            idCounter++
        }
    }

    val traceRoot = TraceRoot(
        traceEvents = traceEvents,
        displayTimeUnit = TimeUnit.MILLISECONDS
    )

    File(path).writeText(json.stringify(traceRoot))
}
