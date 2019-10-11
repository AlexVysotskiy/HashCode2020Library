package common

import common.model.FileNode

class ResultCalculator {

    fun calculateResult(input: Input, output: Output): Long {
        val files = input.nodes
        val servers = hashMapOf<Int, MutableList<FileNode>>()

        val replicatedTime = hashMapOf<FileNode, Int?>()

        output
            .compilationSteps
            .forEach {
                val filesOnTheServer = servers.getOrPut(it.serverIndex.toInt()) { arrayListOf() }
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
