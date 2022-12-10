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

    fun executeNoop(state: State, events: MutableList<Pair<Int, Int>>) {
        state.cycle += 1

        recordEvent(state, events)
    }

    fun executeAddx(state: State, events: MutableList<Pair<Int, Int>>, summand: Int) {
        for (i in 1..2) {
            state.cycle += 1
            if (i == 2) { state.X += summand }

            recordEvent(state, events)
        }
    }

    fun executeCommand(state: State, events: MutableList<Pair<Int, Int>>, command: String) {
        val parts = command.split(" ")
        when (parts[0]) {
            "noop" -> executeNoop(state, events)
            "addx" -> executeAddx(state, events, parts[1].toInt())
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

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}