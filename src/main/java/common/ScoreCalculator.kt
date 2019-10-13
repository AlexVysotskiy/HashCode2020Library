package common

interface ScoreCalculator {
    fun calculateResult(input: Input, output: Output): Long
}
