package solver.urgent

import common.Input
import common.Output
import common.Solver
import common.model.CompilationStep
import java.util.*

class Server(val index: Long, var currentCompilationSum: Long) : Comparable<Server> {

    override fun compareTo(other: Server): Int =
        this.currentCompilationSum.compareTo(other.currentCompilationSum)

}

class ConflictedUrgentSolver : Solver {
    override val name: String
        get() = "UrgentSolver"

    override fun solve(input: Input): Output {
        val compilationSteps = arrayListOf<CompilationStep>()

        val serverCount = input.servers

        // build target c65l (high goal)
        // czu6, cz26, c65l
        val target = input.nodes["c65l"] ?: return Output(compilationSteps)


        val serverSequence = createSmartRoundRobinSequence(serverCount).iterator()

        val servers = List(serverCount.toInt()) {
            Server(it.toLong(), 0)
        }

        target
            .dependencies
            .sortedBy { it.compilation }
            .reversed()
            .forEach {
                val currentServer = servers.min()!!
                compilationSteps.add(CompilationStep(it.name, currentServer.index))
                currentServer.currentCompilationSum += (it.compilation)
            }

        compilationSteps.add(CompilationStep(target.name, servers.min()!!.index))

        return Output(compilationSteps)
    }

    private fun createSmartRoundRobinSequence(serversCount: Long): Sequence<Long> = sequence {
        while (true) {
            for (i in 0 until serversCount) {
                println(i)
                this.yield(i)
            }
            for (i in (serversCount - 1) downTo 0) {
                println(i)
                this.yield(i)
            }
        }
    }
}