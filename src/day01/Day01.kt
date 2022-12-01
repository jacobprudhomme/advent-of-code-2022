import java.io.File

fun main() {
    fun readInput(name: String) = File("src/day01", name)
        .readLines()

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
