package solver.urgent

import common.Input
import common.Output
import common.Solver
import common.model.CompilationStep
import common.model.FileNode

class Server(val index: Long, var currentCompilationSum: Long) : Comparable<Server> {

    override fun compareTo(other: Server): Int =
        this.currentCompilationSum.compareTo(other.currentCompilationSum)

}

class RandomUrgentSolver : Solver {
    override val name: String
        get() = "RandomUrgentSolver"

    override fun solve(input: Input): Output {
        val compilationSteps = arrayListOf<CompilationStep>()

        val serverCount = input.servers

        // build target c65l (high goal)
        // czu6, cz26, c65l, c9a6, cyq6
        val target = input.nodes["czu6"] ?: return Output(compilationSteps)
        val targetCompilationTimeOnOtherServers = 4335 - target.compilation - 2
        val targetCompilationTimeOnFirstServer = 4335 - target.compilation

        val dependencies = target
            .dependencies
            .associateBy { it.name }

//        dependencies.values.sortedBy { it.compilation }.forEach {
//            println(it.compilation)
//        }
//
//        println("!!!!")

        var resultCompletedServers: List<List<FileNode>>? = null

        while (true) {
            val completedServers = arrayListOf<List<FileNode>>()
            val remainingDependencies = dependencies.toMutableMap()
            var maxAttempts = 100_000

            while (completedServers.size < (serverCount - 1) && maxAttempts > 0) {
                val currentServer = arrayListOf<FileNode>()
                val pickedFiles = hashSetOf<String>()

                var sum = -1
                while (sum < targetCompilationTimeOnOtherServers - 5) {
                    val candidates = remainingDependencies.keys - pickedFiles
                    val pickedDependency = candidates.random()
                    currentServer += dependencies.getValue(pickedDependency)
                    pickedFiles.add(pickedDependency)
                    sum = currentServer.sumBy { it.compilation.toInt() }
                }

                val time = currentServer.sumBy { it.compilation.toInt() }
                if (time < targetCompilationTimeOnOtherServers.toInt() + 2) {
                    // completed!
                    pickedFiles.forEach { pickedFile -> remainingDependencies.remove(pickedFile) }
                    completedServers.add(currentServer)
                }

                maxAttempts--
            }

            if (completedServers.size < 29) continue

            val remainingServerSum = remainingDependencies
                .values
                .sumBy { it.compilation.toInt() }

            println("COMPLETED SERVERS with time ${targetCompilationTimeOnOtherServers} +- 1! Completed = ${completedServers.size}")
            println("Remaining server sum = $remainingServerSum")

            resultCompletedServers = completedServers

            if (resultCompletedServers.size == serverCount.toInt()) {
                break
            }
        }

        resultCompletedServers!!.forEachIndexed { index, list ->
            list.forEach {
                compilationSteps.add(CompilationStep(it.name, index.toLong()))
            }
        }

        return Output(compilationSteps)
    }
}