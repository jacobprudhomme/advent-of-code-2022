import java.io.File

fun main() {
    fun readInput(name: String) = File("src/day06", name)
        .readLines()

    fun part1(input: List<String>): Int {
        val WINDOW_SIZE = 4

        var charactersSeen = WINDOW_SIZE - 1
        outerLoop@ for (window in input.first().windowed(WINDOW_SIZE)) {
            ++charactersSeen

            for (char in window.dropLast(1)) {
                if (window.count { it == char } > 1) continue@outerLoop
            }

            break
        }

        return charactersSeen
    }

    fun part2(input: List<String>): Int {
        val WINDOW_SIZE = 14

        var charactersSeen = WINDOW_SIZE - 1
        outerLoop@ for (window in input.first().windowed(WINDOW_SIZE)) {
            ++charactersSeen

            for (char in window.dropLast(1)) {
                if (window.count { it == char } > 1) continue@outerLoop
            }

            break
        }

        return charactersSeen
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}