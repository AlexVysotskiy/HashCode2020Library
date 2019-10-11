package common

import common.model.FileNode
import common.model.TargetValue

data class Input(
    val nodes: Map<String, FileNode>,
    val targets: Map<String, TargetValue>,
    val servers: Long
)
