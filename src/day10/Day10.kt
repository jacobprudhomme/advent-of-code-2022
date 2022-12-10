import java.io.File
import java.lang.IllegalArgumentException

class State(var cycle: Int, var X: Int)

fun main() {
    fun readInput(name: String) = File("src/day10", name)
        .readLines()

    fun recordEvent(state:State, events: MutableList<Pair<Int, Int>>) {
        if ((state.cycle % 40) == 20) {
            events.add(Pair(state.cycle, state.X))
        }
    }

    fun draw(state: State) {
        val writingPosition = (state.cycle - 1) % 40

        if (writingPosition in ((state.X-1)..(state.X+1))) {
            print("#")
        } else {
            print(".")
        }

        if (writingPosition == 39) {
            println()
        }
    }

    fun executeNoop(
        state: State,
        events: MutableList<Pair<Int, Int>>,
        drawPixel: Boolean
    ) {
        if (drawPixel) draw(state)

        state.cycle += 1

        if (!drawPixel) recordEvent(state, events)
    }

    fun executeAddx(
        state: State,
        events: MutableList<Pair<Int, Int>>,
        summand: Int,
        drawPixel: Boolean
    ) {
        for (i in 1..2) {
            if (drawPixel) draw(state)

            state.cycle += 1
            if (i == 2) { state.X += summand }

            if (!drawPixel) recordEvent(state, events)
        }
    }

    fun executeCommand(
        state: State,
        events: MutableList<Pair<Int, Int>>,
        command: String,
        drawPixel: Boolean = false
    ) {
        val parts = command.split(" ")
        when (parts[0]) {
            "noop" -> executeNoop(state, events, drawPixel)
            "addx" -> executeAddx(state, events, parts[1].toInt(), drawPixel)
            else   -> throw IllegalArgumentException("We should never get here")
        }
    }

    fun part1(input: List<String>): Int {
        val state = State(1, 1)
        val events = mutableListOf<Pair<Int, Int>>()
        for (command in input) {
            executeCommand(state, events, command)
        }

        return events.fold(0) { acc, (cycle, X) -> acc + (cycle * X) }
    }

    fun part2(input: List<String>) {
        val state = State(1, 1)
        val events = mutableListOf<Pair<Int, Int>>()
        for (command in input) {
            executeCommand(state, events, command, true)
        }
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}