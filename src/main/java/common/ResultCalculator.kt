package common

import common.model.Node

class ResultCalculator {

    fun calculateResult(input: Input, output: Output): Long {
        val files = input.nodes
        val servers = hashMapOf<Int, MutableList<Node>>()

        val replicatedTime = hashMapOf<Node, Int?>()

        output
            .compilationSteps
            .forEach {
                val filesOnTheServer = servers.getOrPut(it.serverIndex) { arrayListOf() }
                filesOnTheServer.add(files[it.name]!!)
            }

        servers
            .forEach { serverIndex, files ->
                var currentTime = 0
                files.forEach {

                }
            }

        val maxDeadline = input.targets.maxBy { it.deadline }!!.deadline

        for (time in 0 until maxDeadline) {

        }

        for (serverIndex in 0 until input.servers) {

        }

        return 0
    }

}
