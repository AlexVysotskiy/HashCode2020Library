package utils

import common.helpers.CostValue
import common.helpers.Knapsack2
import common.helpers.KnapsackItem

object Knapsack {

    fun <T> solve(volume: Int, items: List<T>, mapper: (T) -> CostValue): List<T> {
        return printknapSack(
                volume,
                items.map { KnapsackItem(it, mapper.invoke(it).cost, mapper.invoke(it).value) }
        ).map { it.payload }
    }

    fun <T> printknapSack(W: Int, items: List<KnapsackItem<T>>): List<KnapsackItem<T>> {

        val wt = items.map { it.volume.toInt() }.toIntArray()
        val `val` = items.map { it.cost.toInt() }.toIntArray()
        val n = items.size

        var i: Int
        var w: Int
        val K = Array(n + 1) { IntArray(W + 1) }

        // Build table K[][] in bottom up manner
        i = 0
        while (i <= n) {
            w = 0
            while (w <= W) {
                if (i == 0 || w == 0)
                    K[i][w] = 0
                else if (wt[i - 1] <= w)
                    K[i][w] = Math.max(`val`[i - 1] + K[i - 1][w - wt[i - 1]], K[i - 1][w])
                else
                    K[i][w] = K[i - 1][w]
                w++
            }
            i++
        }

        // stores the result of Knapsack
        var res = K[n][W]

        w = W
        i = n
        val list = mutableListOf<KnapsackItem<T>>()
        while (i > 0 && res > 0) {

            // either the result comes from the top
            // (K[i-1][w]) or from (val[i-1] + K[i-1]
            // [w-wt[i-1]]) as in Knapsack table. If
            // it comes from the latter one/ it means
            // the item is included.
            if (res == K[i - 1][w]) {
                i--
                continue
            } else {
                // This item is included.
                list.add(items[i - 1])
                // Since this weight is included its
                // value is deducted
                res = res - `val`[i - 1]
                w = w - wt[i - 1]
            }
            i--
        }
        return list
    }

    // Driver code
    @JvmStatic
    fun main(arg: Array<String>) {
        print(solve(
            50, listOf(
                Pair(60, 10),
                Pair(100, 20),
                Pair(120, 30)
            )
        ) { CostValue(it.first.toLong(), it.second.toLong()) })

        print(
            Knapsack2.solve(
            50, listOf(
                Pair(60, 10),
                Pair(100, 20),
                Pair(120, 30)
            )
        ) { CostValue(it.first.toLong(), it.second.toLong()) })
    }

}