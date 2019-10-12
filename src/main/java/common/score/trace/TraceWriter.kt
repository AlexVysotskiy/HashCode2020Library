package common.score.trace

import common.model.FileNode
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
fun writeChromeTrace(
    nodes: List<FileNode>,
    computationNodes: List<ComputationNode>,
    targets: List<TargetValue>,
    path: String,
    enableArrows: Boolean = false
) {
    val json = Json(JsonConfiguration.Default)

    val targetNames = targets.map { it.name }.toHashSet()
    val traceEvents = mutableListOf<TraceEvent>()

    var idCounter = 0L

    val fileToTrace = hashMapOf<FileNode, MutableList<ComputationTraceEvent>>()

    computationNodes.forEach { node ->
        node.traceEvents.forEach { traceEvent ->
            fileToTrace.getOrPut(traceEvent.fileNode) { arrayListOf() }.add(traceEvent)

            val isTarget = traceEvent.fileNode.name in targetNames
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                ph = Phase.DurationStart,
                name = traceEvent.fileNode.name,
                ts = traceEvent.compilationBegin.toDouble(),
                cname = if (isTarget) "bad" else "good",
                cat = if (isTarget) "target" else "node",
                args = mapOf(
                    "replicationTime" to traceEvent.fileNode.replication.toString(),
                    "compilationTime" to traceEvent.fileNode.compilation.toString(),
                    "isTarget" to isTarget.toString(),
                    "dependenciesCount" to traceEvent.fileNode.dependencies.size.toString(),
                    "parentsCount" to traceEvent.fileNode.parents.size.toString()
                )
            )
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                ph = Phase.DurationEnd,
                name = traceEvent.fileNode.name,
                ts = traceEvent.compilationEnd.toDouble()
            )

            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                id = idCounter,
                ph = Phase.AsyncStart,
                name = "replication",
                ts = traceEvent.compilationEnd.toDouble(),
                args = mapOf(
                    "fileName" to traceEvent.fileNode.name
                )
            )
            traceEvents += TraceEvent(
                pid = 0,
                tid = node.serverIndex,
                id = idCounter,
                ph = Phase.AsyncEnd,
                name = "replication",
                ts = traceEvent.replicationEnd.toDouble()
            )

            idCounter++
        }
    }

    val arrowEvents = mutableListOf<TraceEvent>()

    if (enableArrows) {

        nodes
            .asSequence()
            .map { currentNode ->
                fileToTrace[currentNode] to currentNode
                    .parents
                    .map { parentNode -> fileToTrace[parentNode] }
                    .filter { parentTraces -> !parentTraces.isNullOrEmpty() }
            }
            .filter { (nodeTraces, parentTraces) ->
                !nodeTraces.isNullOrEmpty() && !parentTraces.isNullOrEmpty()
            }
            .forEach { (nodeTraces, parentTraces) ->
                nodeTraces!!.forEach { nodeTrace ->
                    parentTraces.forEach { traces ->
                        traces!!.forEach { traceEvent ->
                            // start
                            arrowEvents += TraceEvent(
                                name = "arrow",
                                cat = "dependency",
                                pid = 0,
                                tid = nodeTrace.serverIndex,
                                id = idCounter,
                                ph = Phase.FlowStart,
                                ts = nodeTrace.compilationBegin.toDouble()
                            )

                            // end
                            arrowEvents += TraceEvent(
                                name = "arrow",
                                cat = "dependency",
                                pid = 0,
                                tid = traceEvent.serverIndex,
                                id = idCounter,
                                ph = Phase.FlowEnd,
                                ts = traceEvent.compilationBegin.toDouble()
                            )

                            idCounter++
                        }
                    }
                }
            }
    }

    val events = traceEvents + if (enableArrows) arrowEvents else emptyList()

    val traceRoot = TraceRoot(
        traceEvents = events,
        displayTimeUnit = TimeUnit.MILLISECONDS
    )

    File(path).writeText(json.stringify(traceRoot))
}
