package common

import common.model.FileNode
import common.model.TargetValue

data class Input(
    val nodes: Map<String, FileNode>,
    val targets: List<TargetValue>,
    val servers: Long
)
