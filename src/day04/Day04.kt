import java.io.File

fun main() {
    fun readInput(name: String) = File("src/day04", name)
        .readLines()

    fun processInput(line: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val (range1, range2, _) = line.split(",").map { range ->
            range.split("-").map { it.toInt() }.let { (start, end, _) ->
                Pair(start, end)
            }
        }

        return Pair(range1, range2)
    }

    fun sign(n: Int): Int =
        if      (n > 0) { 1 }
        else if (n < 0) { -1 }
        else            { 0 }

    fun Pair<Int, Int>.contains(otherRange: Pair<Int, Int>): Boolean {
        val startDiff = this.first - otherRange.first
        val endDiff = this.second - otherRange.second

        return (sign(startDiff) >= 0) and (sign(endDiff) <= 0)
    }

    fun Pair<Int, Int>.overlaps(otherRange: Pair<Int, Int>): Boolean =
        (this.first <= otherRange.second) and (otherRange.first <= this.second)

    fun part1(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (range1, range2) = processInput(line)

            if (range1.contains(range2) or range2.contains(range1)) {
                ++count
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (range1, range2) = processInput(line)

            if (range1.overlaps(range2)) {
                ++count
            }
        }

        return count
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
