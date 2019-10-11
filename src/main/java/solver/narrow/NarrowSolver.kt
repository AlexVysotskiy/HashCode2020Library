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

        val c = hashSetOf<FileNode>()

        input.nodes
            .forEach { (_, node) ->
                if (node.dependencies.size > 1) { // 100 dependencies
                    c.add(node)
                }
            }

        val q = hashSetOf<FileNode>()
        val r = hashSetOf<FileNode>()
        val a = hashSetOf<FileNode>()

        c.forEach {
            it.dependencies.forEach { cDependency ->
                q.add(cDependency)

                cDependency.dependencies.forEach { qDependency ->
                    r.add(qDependency)
                }
            }
        }

        r.forEach {
            it.dependencies.forEach {
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

        // TODO: add q evenly on servers by dependencies
        // TODO: add c evenly on servers by dependencies

        return Output(compilationSteps)
    }

}
