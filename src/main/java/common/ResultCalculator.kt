package common

import common.model.CompilationStep
import common.model.FileNode
import common.model.TargetValue

class ResultCalculator {

    private lateinit var storeOfCompiledFiles: Store

    private lateinit var files: Map<String, FileNode>
    private var serverCount: Long = 0
    private lateinit var compilationSteps: List<CompilationStep>

    fun calculateResult(input: Input, output: Output): Long {
        files = input.nodes
        serverCount = input.servers
        compilationSteps = output.compilationSteps
        storeOfCompiledFiles = Store(input.targets.values.toList())

        val maxDeadline = input.targets.values.maxBy { it.deadline }!!.deadline

        val computationNodes = createComputationNodes()
        for (timeTick in 0..maxDeadline) {
            // We should collect already finished files (potential deps for current tick)
            loopAllComputationNodes(computationNodes, timeTick)
            loopAllComputationNodes(computationNodes, timeTick)
        }

        return storeOfCompiledFiles.resultScore
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

class Store(private val targets: List<TargetValue>) {
    private val compiledTasks: MutableList<Task> = mutableListOf()

    var resultScore = 0L
        private set

    fun checkDependencyAvailability(node: ComputationNode, fileNode: FileNode, currentTime: Long): Boolean {
        val deps = fileNode.dependencies
        return deps.all {
            val depName = it.name

            // The same file can be compiled on different servers
            val compiledDeps = compiledTasks.filter { it.fileNode.name == depName }

            // We need to check each compiled task even it's the same file
            compiledDeps.forEach { compiledDep ->
                if (compiledDep.serverIndex == node.serverIndex) return@all true
                if (compiledDep.timeOfGlobalAvailability <= currentTime) return@all true
            }

            return false
        }
    }

    fun addCompiledTask(task: Task) {
        compiledTasks.add(task)
        resultScore += calculateScore(task)
    }

    private fun calculateScore(task: Task): Long {
        val target = targets.find { it.name == task.fileNode.name } ?: return 0

        return if (task.timeOfFinishCompiling > target.deadline) {
            0
        } else {
            target.deadline - task.timeOfFinishCompiling + target.goal
        }
    }

    fun printDebug() {
        compiledTasks.forEach {
            System.out.println("${it.fileNode.name}: time=${it.timeOfFinishCompiling}, repl=${it.timeOfGlobalAvailability}")
        }
    }
}

data class ComputationNode(
    val serverIndex: Long,
    val queue: MutableList<FileNode>
) {
    var isShutdown: Boolean = false
        private set

    private var processingTask: Task? = null

    fun isBusy(currentTime: Long): Boolean =
        processingTask?.let { it.timeOfFinishCompiling > currentTime } ?: false

    fun finishTaskIfPossible(currentTime: Long): Task? =
        processingTask?.takeIf { it.timeOfFinishCompiling <= currentTime }?.also {
            processingTask = null
        }

    fun doShutdown() {
        isShutdown = true
    }

    fun proceedNext(currentTime: Long) {
        val fileNode = queue.first()
        processingTask = Task(
            fileNode = fileNode,
            serverIndex = serverIndex,
            timeOfFinishCompiling = currentTime + fileNode.compilation,
            timeOfGlobalAvailability = currentTime + fileNode.compilation + fileNode.replication
        )
        queue.remove(fileNode)
    }
}

data class Task(
    val fileNode: FileNode,
    val serverIndex: Long,
    val timeOfFinishCompiling: Long,
    val timeOfGlobalAvailability: Long
)
