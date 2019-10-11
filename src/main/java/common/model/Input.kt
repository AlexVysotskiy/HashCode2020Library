package common.model

data class Node(
    val name: String,
    val compilation: Long,
    val replication: Long,
    val dependencies: List<Node>
)

data class TargetValue(
    val name: String,
    val deadline: Long,
    val goal: Long
)
