import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

typealias Pos = Pair<Int, Int>

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
}

fun main() {
    fun readInput(name: String) = File("src/day09", name)
        .readLines()

    fun convertToDirection(dirStr: String): Direction =
        when (dirStr) {
            "U"  -> Direction.UP
            "R"  -> Direction.RIGHT
            "D"  -> Direction.DOWN
            "L"  -> Direction.LEFT
            else -> throw IllegalArgumentException("We should never get here")
        }

    fun moveHead(headPos: Pos, dir: Direction): Pos =
        when (dir) {
            Direction.UP    -> headPos.let { (x, y) -> Pair(x, y+1) }
            Direction.RIGHT -> headPos.let { (x, y) -> Pair(x+1, y) }
            Direction.DOWN  -> headPos.let { (x, y) -> Pair(x, y-1) }
            Direction.LEFT  -> headPos.let { (x, y) -> Pair(x-1, y) }
        }

    fun moveTail(headPos: Pos, tailPos: Pos): Pos {
        val horizontalDist = abs(headPos.first - tailPos.first)
        val verticalDist = abs(headPos.second - tailPos.second)

        return if ((horizontalDist == 2) or (verticalDist == 2)) {
            if (horizontalDist == 0) {
                tailPos.let { (x, y) -> Pair(x, (y + headPos.second).div(2)) }
            } else if (verticalDist == 0) {
                tailPos.let { (x, y) -> Pair((x + headPos.first).div(2), y) }
            } else if (horizontalDist == 1) {
                tailPos.let { (_, y) -> Pair(headPos.first, (y + headPos.second).div(2)) }
            } else if (verticalDist == 1) {
                tailPos.let { (x, _) -> Pair((x + headPos.first).div(2), headPos.second) }
            } else {
                tailPos.let { (x, y) -> Pair((x + headPos.first).div(2), (y + headPos.second).div(2)) }
            }
        } else {
            tailPos
        }
    }

    fun move(headPos: Pos, tailPos: Pos, dir: Direction): Pair<Pos, Pos> {
        val nextHeadPos = moveHead(headPos, dir)
        val nextTailPos = moveTail(nextHeadPos, tailPos)

        return Pair(nextHeadPos, nextTailPos)
    }

    fun part1(input: List<String>): Int {
        var currHeadPosition = Pair(0, 0)
        var currTailPosition = Pair(0, 0)
        val seenPositions = mutableSetOf(currTailPosition)
        for (line in input) {
            val (dir, steps) = line.split(" ").let { (dirStr, stepsStr, _) ->
                Pair(convertToDirection(dirStr), stepsStr.toInt())
            }

            repeat(steps) {
                val (nextHeadPosition, nextTailPosition) = move(currHeadPosition, currTailPosition, dir)
                currHeadPosition = nextHeadPosition
                currTailPosition = nextTailPosition

                seenPositions.add(currTailPosition)
            }
        }

        return seenPositions.count()
    }

    fun part2(input: List<String>): Int {
        val currPositions = MutableList(10) { Pair(0, 0) }
        val seenPositions = mutableSetOf(currPositions.last())
        for (line in input) {
            val (dir, steps) = line.split(" ").let { (dirStr, stepsStr, _) ->
                Pair(convertToDirection(dirStr), stepsStr.toInt())
            }

            repeat(steps) {
                currPositions[0] = moveHead(currPositions[0], dir)
                for (knotIndex in currPositions.indices.drop(1)) {
                    currPositions[knotIndex] = moveTail(currPositions[knotIndex-1], currPositions[knotIndex])
                }

                seenPositions.add(currPositions.last())
            }
        }

        return seenPositions.count()
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}