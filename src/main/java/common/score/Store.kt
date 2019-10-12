package common.score

import common.model.FileNode
import common.model.TargetValue

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
