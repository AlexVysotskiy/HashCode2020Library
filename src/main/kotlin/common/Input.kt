package common

import common.model.Node
import common.model.TargetValue

data class Input(
    val nodes: Map<String, Node>,
    val targets: List<TargetValue>,
    val servers: Long
)
