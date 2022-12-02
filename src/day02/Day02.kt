import java.io.File
import java.lang.IllegalArgumentException

enum class Result(val score: Int) {
    LOSE(0),
    DRAW(3),
    WIN(6),
}

enum class Move(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun against(other: Move): Result = when (other.value) {
        ((value + 1) % 3) + 1 -> Result.WIN
        value                 -> Result.DRAW
        else                  -> Result.LOSE
    }
}

fun main() {
    fun readInput(name: String) = File("src/day02", name)
        .readLines()

    fun decryptMove(encryptedMove: String): Move = when (encryptedMove) {
        "A", "X" -> Move.ROCK
        "B", "Y" -> Move.PAPER
        "C", "Z" -> Move.SCISSORS
        else     -> throw IllegalArgumentException("We should never get here")
    }

    fun part1(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val (theirMove, myMove, _) = line.split(" ").map { decryptMove(it) }
            score += myMove.value + myMove.against(theirMove).score
        }

        return score
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
