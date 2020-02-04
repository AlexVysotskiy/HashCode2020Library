package solver.lukaville

object GpuSolverKernel {
    @JvmField
    val source = this::class
        .java
        .getResourceAsStream("/kernel.c")
        .bufferedReader()
        .readText()
}
