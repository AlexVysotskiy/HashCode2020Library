package common.helpers

data class KnapsackItem<T>(
    val payload: T,
    val cost: Long,
    val volume: Long
)