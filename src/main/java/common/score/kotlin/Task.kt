package common.score.kotlin

import common.model.FileNode

data class Task(
    val fileNode: FileNode,
    val serverIndex: Long,
    val timeOfFinishCompiling: Long,
    val timeOfGlobalAvailability: Long
)