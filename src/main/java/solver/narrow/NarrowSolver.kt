package solver.narrow

import common.Input
import common.Output
import common.Solver
import common.model.CompilationStep
import common.model.FileNode

class NarrowSolver(override val name: String = "Narrow") : Solver {

    override fun solve(input: Input): Output {
        val compilationSteps = arrayListOf<CompilationStep>()

        val serverCount = input.servers

        // "c" -> "q" -> "r" -> "a"
        val c = hashSetOf<FileNode>() // 50 "c" with 100 dependencies
        val q = hashSetOf<FileNode>() // 5000 "q"
        val r = hashSetOf<FileNode>() // 50 "r"
        val a = hashSetOf<FileNode>() // 50 "a"

        input.nodes
            .values
            .filter { it.dependencies.size > 1 } // 50 "c" targets with 100 dependencies
            .sortedBy {
                val depsSum = it.dependencies.sumBy { fileNode -> fileNode.compilation.toInt() }
                (input.targets[it.name]?.goal ?: -100) - depsSum
            }
            .reversed()
            .take(35)
            .forEach { node -> c.add(node) }

        c.forEach {
            it.dependencies.forEach { cDependency ->
                q.add(cDependency)

                cDependency.dependencies.forEach { qDependency ->
                    r.add(qDependency)
                }
            }
        }

        r.forEach { rNode ->
            rNode.dependencies.forEach {
                a.add(it)
            }
        }

        // add A
        a
            .forEach { node ->
                for (i in 0 until serverCount) {
                    compilationSteps.add(CompilationStep(node.name, i))
                }
            }

        // add R
        r
            .forEach { node ->
                for (i in 0 until serverCount) {
                    compilationSteps.add(CompilationStep(node.name, i))
                }
            }

        // round-robin all Qs
        var currentServer = 0L
        q.forEach {
            compilationSteps.add(CompilationStep(it.name, currentServer))
            currentServer = (currentServer + 1) % serverCount
        }

        // round-robin all Cs
        c.forEach {
            compilationSteps.add(CompilationStep(it.name, currentServer))
            currentServer = (currentServer + 1) % serverCount
        }

        return Output(compilationSteps)
    }

}
