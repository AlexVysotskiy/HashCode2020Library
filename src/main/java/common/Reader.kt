package common

import common.model.FileNode
import common.model.TargetValue
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Long.parseLong
import java.util.*

object Reader {

    @JvmStatic
    fun readInput(file: InputStream): Input {
        val bufferReader = BufferedReader(InputStreamReader(file))
        val stringTokenizer = StringTokenizer(bufferReader.readLine())

        val nodes = HashMap<String, FileNode>()

        val files = parseLong(stringTokenizer.nextToken())
        val targets = parseLong(stringTokenizer.nextToken())
        val servers = parseLong(stringTokenizer.nextToken())

        for (i in 0 until files) {
            val stringTokenizer = StringTokenizer(bufferReader.readLine())
            val name = stringTokenizer.nextToken()
            val compilation = parseLong(stringTokenizer.nextToken())
            val replication = parseLong(stringTokenizer.nextToken())

            val dependencies = mutableListOf<FileNode>()
            val depsTokenizer = StringTokenizer(bufferReader.readLine())
            val depNum = parseLong(depsTokenizer.nextToken())
            for (j in 0 until depNum) {
                val depName = depsTokenizer.nextToken()
                val dependency = nodes[depName]!!
                dependencies.add(dependency)
            }

            val node = FileNode(
                name = name,
                compilation = compilation,
                replication = replication,
                dependencies = dependencies
            )
            dependencies.forEach {
                (it.parents as MutableList<FileNode>).add(node)
            }
            nodes[name] = node
        }

        val goals = mutableMapOf<String, TargetValue>()
        for (i in 0 until targets) {
            val stringTokenizer = StringTokenizer(bufferReader.readLine())
            val name = stringTokenizer.nextToken()
            val deadline = parseLong(stringTokenizer.nextToken())
            val goal = parseLong(stringTokenizer.nextToken())

            val target = TargetValue(
                name = name,
                deadline = deadline,
                goal = goal
            )
            goals[name] = target
        }

        return Input(
            nodes,
            goals,
            servers
        )
    }
}
