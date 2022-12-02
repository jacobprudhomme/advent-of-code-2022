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

    fun obtainsAgainst(result: Result): Move = when (result) {
        Result.WIN  -> fromValue((value % 3) + 1)
        Result.LOSE -> fromValue(((value + 1) % 3) + 1)
        else        -> this
    }

    companion object {
        fun fromValue(value: Int): Move = when (value) {
            1    -> ROCK
            2    -> PAPER
            3    -> SCISSORS
            else -> throw IllegalArgumentException("We should never get here")
        }
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

    fun decryptResult(encryptedResult: String): Result = when (encryptedResult) {
        "X"  -> Result.LOSE
        "Y"  -> Result.DRAW
        "Z"  -> Result.WIN
        else -> throw IllegalArgumentException("We should never get here")
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
        var score = 0
        for (line in input) {
            val (theirMoveStr, myResultStr, _) = line.split(" ")
            val theirMove = decryptMove(theirMoveStr)
            val myResult = decryptResult(myResultStr)
            val myMove = theirMove.obtainsAgainst(myResult)

            score += myMove.value + myResult.score
        }

        return score
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
