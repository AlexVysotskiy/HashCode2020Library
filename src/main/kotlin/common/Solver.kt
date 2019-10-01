package common

interface Solver {
    val name: String
    fun solve(input: Input): Output
}
