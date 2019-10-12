package common.model

data class FileNode(
    val name: String,
    val compilation: Long,
    val replication: Long,
    val dependencies: List<FileNode>
) {
    val parents: List<FileNode> = mutableListOf()
}

data class TargetValue(
    val name: String,
    val deadline: Long,
    val goal: Long
)
