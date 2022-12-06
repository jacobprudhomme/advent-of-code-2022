import java.io.File

typealias CrateStacks = Array<ArrayDeque<Char>>
typealias CraneProcedure = List<Triple<Int, Int, Int>>

fun main() {
    fun readInput(name: String) = File("src/day05", name)
        .readLines()

    fun processInput(input: List<String>): Pair<CrateStacks, CraneProcedure> {
        val craneProcedureStrs = input.takeLastWhile { it.isNotEmpty() }
        val craneProcedure: CraneProcedure = craneProcedureStrs.map { procedure ->
            Regex("\\d+")
                .findAll(procedure)
                .map(MatchResult::value)
                .map(String::toInt)
                .toList()
                .let { (num, from, to, _) -> Triple(num, from-1, to-1) }
        }

        val (crateStacksStrs, stackNumbersStr) = input
            .takeWhile { it.isNotEmpty() }
            .let { Pair(it.dropLast(1).reversed(), it.last()) }
        val numStacks = Regex("\\d+")
            .findAll(stackNumbersStr)
            .count()
        val crateStacks = Array(numStacks) { ArrayDeque<Char>() }
        for (line in crateStacksStrs) {
            val cratesAtLevel = line.chunked(4) { it.filter(Char::isLetter) }
            cratesAtLevel.forEachIndexed { i, crate ->
                if (crate.isNotEmpty()) {
                    crateStacks[i].addLast(crate.first())
                }
            }
        }

        return Pair(crateStacks, craneProcedure)
    }

    fun part1(input: List<String>): String {
        val (crateStacks, craneProcedure) = processInput(input)

        for ((num, from, to) in craneProcedure) {
            repeat(num) {
                val crate = crateStacks[from].removeLast()
                crateStacks[to].addLast(crate)
            }
        }

        return crateStacks.map { it.last() }.joinToString("")
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}