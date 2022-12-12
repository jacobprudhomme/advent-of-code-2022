import java.io.File
import java.lang.IllegalArgumentException
import java.util.PriorityQueue

class Monkey(
    val heldItems: MutableList<Int>,
    val applyOperation: (Int) -> Int,
    val decide: (Int) -> Int
) {
    var inspectedItems = 0

    fun inspect() { ++inspectedItems }
}

fun main() {
    fun readInput(name: String) = File("src/day11", name)
        .readLines()

    fun iterateInput(input: List<String>) = sequence {
        var currMonkey = input.takeWhile { it.isNotBlank() }.drop(1)
        var rest = input.dropWhile { it.isNotBlank() }.drop(1)

        while (currMonkey.isNotEmpty()) {
            yield(currMonkey)

            currMonkey = rest.takeWhile { it.isNotBlank() }
                .let { if (it.isNotEmpty()) { it.drop(1) } else { it } }
            rest = rest.dropWhile { it.isNotBlank() }
                .let { if (it.isNotEmpty()) { it.drop(1) } else { it } }
        }
    }

    fun processOperation(
        opStr: String,
        leftArg: String,
        rightArg: String
    ): (Int) -> Int {
        val operation: (Int, Int) -> Int = when (opStr) {
            "+"  -> { m, n -> m + n }
            "*"  -> { m, n -> m * n }
            else -> throw IllegalArgumentException("We should never get here")
        }

        return if ((leftArg == "old") and (rightArg == "old")) {
            { n -> operation(n, n) }
        } else if (leftArg == "old") {
            { n -> operation(n, rightArg.toInt()) }
        } else if (rightArg == "old") {
            { n -> operation(leftArg.toInt(), n) }
        } else {
            { _ -> operation(leftArg.toInt(), rightArg.toInt()) }
        }
    }

    fun processInput(input: List<String>): Array<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        for (monkeyStr in iterateInput(input)) {
            val initItems = monkeyStr[0].substring(18).split(", ").map(String::toInt)
            val (leftArg, op, rightArg, _) = monkeyStr[1].substring(19).split(" ")
            val testValue = monkeyStr[2].substring(21).toInt()
            val trueResult = monkeyStr[3].substring(29).toInt()
            val falseResult = monkeyStr[4].substring(30).toInt()

            val monkey = Monkey(
                initItems.toMutableList(),
                processOperation(op, leftArg, rightArg)
            ) { n -> if (n % testValue == 0) { trueResult } else { falseResult } }
            monkeys.add(monkey)
        }

        return monkeys.toTypedArray()
    }

    fun doRound(monkeys: Array<Monkey>) {
        for (monkey in monkeys) {
            while (monkey.heldItems.isNotEmpty()) {
                val currItem = monkey.heldItems.removeFirst()
                monkey.inspect()

                val worryLevel = monkey.applyOperation(currItem).div(3)
                val monkeyToCatch = monkey.decide(worryLevel)

                monkeys[monkeyToCatch].heldItems.add(worryLevel)
            }
        }
    }

    fun multiplyTwoLargest(monkeys: Array<Monkey>): Int {
        val monkeysByInspectedElements = PriorityQueue(monkeys.map { -it.inspectedItems })  // Induce max-queue

        return monkeysByInspectedElements.poll() * monkeysByInspectedElements.poll()
    }

    fun part1(input: List<String>): Int {
        val monkeys = processInput(input)

        repeat(20) {
            doRound(monkeys)
        }

        return multiplyTwoLargest(monkeys)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}