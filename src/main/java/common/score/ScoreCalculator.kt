package common.score

import common.Input
import common.Output
import common.model.CompilationStep
import common.model.FileNode
import common.model.TargetValue
import common.score.trace.writeChromeTrace
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.stringify
import me.tongfei.progressbar.ProgressBar
import trace.Phase
import trace.TraceEvent
import trace.TraceRoot
import java.io.File
import java.util.concurrent.TimeUnit

class ResultCalculator {

    private lateinit var storeOfCompiledFiles: Store

    private lateinit var files: Map<String, FileNode>
    private var serverCount: Long = 0
    private lateinit var compilationSteps: List<CompilationStep>

    private lateinit var computationNodes: List<ComputationNode>
    private lateinit var targets: List<TargetValue>

    fun calculateResult(input: Input, output: Output): Long {
        files = input.nodes
        serverCount = input.servers
        compilationSteps = output.compilationSteps
        targets = input.targets.values.toList()
        storeOfCompiledFiles = Store(targets)

        val maxDeadline = input.targets.values.maxBy { it.deadline }!!.deadline

        this.computationNodes = createComputationNodes()
        for (timeTick in ProgressBar.wrap((0..maxDeadline).toList(), "Ticks")) {
            // We should collect already finished files (potential deps for current tick)
            loopAllComputationNodes(computationNodes, timeTick)
            loopAllComputationNodes(computationNodes, timeTick)
        }

        return storeOfCompiledFiles.resultScore
    }

    fun writeTrace(path: String) {
        writeChromeTrace(computationNodes, targets, path)
    }

    private fun loopAllComputationNodes(computationNodes: List<ComputationNode>, timeTick: Long) {
        for (computationNode in computationNodes) {
            if (computationNode.isBusy(timeTick) || computationNode.isShutdown) continue

            computationNode.finishTaskIfPossible(timeTick)?.let { storeOfCompiledFiles.addCompiledTask(it) }

            if (computationNode.queue.isEmpty()) {
                computationNode.doShutdown()
            } else {
                val next = computationNode.queue.first()
                if (storeOfCompiledFiles.checkDependencyAvailability(computationNode, next, timeTick)) {
                    computationNode.proceedNext(timeTick)
                }
            }
        }
    }

    private fun createComputationNodes(): List<ComputationNode> {
        val computationNodes = mutableListOf<ComputationNode>()
        (0..serverCount).map { serverIndex ->
            val stepsForServer = compilationSteps.filter { it.serverIndex == serverIndex }
            val filesForServer = getFileNodes(stepsForServer)
            computationNodes.add(ComputationNode(serverIndex, filesForServer.toMutableList()))
        }

        return computationNodes
    }

    private fun getFileNodes(steps: List<CompilationStep>) =
        steps.map { step -> files.values.find { node -> node.name == step.name }!! }
}
