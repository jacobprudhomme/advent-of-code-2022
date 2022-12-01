import java.io.File

fun main() {
    fun readInput(name: String) = File("src/day01", name)
        .readLines()

    fun part1(input: List<String>): Int {
        var maxSoFar = 0
        var currentSum = 0
        for (line in input) {
            if (line.isEmpty()) {
                if (currentSum > maxSoFar) {
                    maxSoFar = currentSum
                }

                currentSum = 0
            } else {
                currentSum += line.toInt()
            }
        }

        return if (currentSum > maxSoFar) currentSum else maxSoFar
    }

    fun part2(input: List<String>): Int {
        val maxesSoFar = intArrayOf(0, 0, 0)
        var currentSum = 0
        for (line in input) {
            if (line.isEmpty()) {
                val (idx, smallestMaxSoFar) = maxesSoFar.withIndex().minBy { (_, maxVal) -> maxVal }
                if (currentSum > smallestMaxSoFar) {
                    maxesSoFar[idx] = currentSum
                }

                currentSum = 0
            } else {
                currentSum += line.toInt()
            }
        }

        val (idx, smallestMaxSoFar) = maxesSoFar.withIndex().minBy { (_, maxVal) -> maxVal }
        if (currentSum > smallestMaxSoFar) {
            maxesSoFar[idx] = currentSum
        }

        return maxesSoFar.sum()
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
