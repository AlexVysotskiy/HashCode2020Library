package common.score.trace

import common.model.FileNode

class ComputationTraceEvent(
    val serverIndex: Long,
    val fileNode: FileNode,
    val compilationBegin: Long,
    val compilationEnd: Long,
    val replicationEnd: Long
)

