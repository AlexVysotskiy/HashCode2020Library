package common

import common.model.Node
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

        val nodes = HashMap<String, Node>()

        val files = parseLong(stringTokenizer.nextToken())
        val targets = parseLong(stringTokenizer.nextToken())
        val servers = parseLong(stringTokenizer.nextToken())

        for (i in 0 until files) {
            val stringTokenizer = StringTokenizer(bufferReader.readLine())
            val name = stringTokenizer.nextToken()
            val compilation = parseLong(stringTokenizer.nextToken())
            val replication = parseLong(stringTokenizer.nextToken())

            val dependencies = mutableListOf<Node>()
            val depsTokenizer = StringTokenizer(bufferReader.readLine())
            val depNum = parseLong(depsTokenizer.nextToken())
            for (j in 0 until depNum) {
                val depName = depsTokenizer.nextToken()
                val dependency = nodes[depName]!!
                dependencies.add(dependency)
            }

            val node = Node(
                name = name,
                compilation = compilation,
                replication = replication,
                dependencies = dependencies
            )
            nodes[name] = node
        }

        val goals = mutableListOf<TargetValue>()
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
            goals.add(target)
        }

        return Input(
            nodes,
            goals,
            servers
        )
    }
}
