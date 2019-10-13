package common.score.kotlin

import common.model.FileNode
import common.score.kotlin.trace.ComputationTraceEvent

data class ComputationNode(
    val serverIndex: Long,
    val queue: MutableList<FileNode>
) {
    var isShutdown: Boolean = false
        private set

    private var processingTask: Task? = null

    private val _traceEvents: MutableList<ComputationTraceEvent> = arrayListOf()
    val traceEvents: List<ComputationTraceEvent> = _traceEvents

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

        _traceEvents += ComputationTraceEvent(
            serverIndex,
            fileNode,
            currentTime,
            processingTask!!.timeOfFinishCompiling,
            processingTask!!.timeOfGlobalAvailability
        )
    }
}
