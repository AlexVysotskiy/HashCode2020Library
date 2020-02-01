package common

interface Optimizer {
    val name: String
    fun optimize(input: Input, output: Output): Output
}
