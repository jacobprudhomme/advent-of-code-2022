import java.io.File
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.util.PriorityQueue

class Monkey(
    val heldItems: MutableList<Int>,
    val applyOperation: (Int) -> BigInteger,
    val decide: (Int) -> Int
) {
    var inspectedItems: BigInteger = BigInteger.ZERO

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
    ): (Int) -> BigInteger {
        val operation: (BigInteger, BigInteger) -> BigInteger = when (opStr) {
            "+"  -> { m, n -> m + n }
            "*"  -> { m, n -> m * n }
            else -> throw IllegalArgumentException("We should never get here")
        }

        return if ((leftArg == "old") and (rightArg == "old")) {
            { n -> operation(n.toBigInteger(), n.toBigInteger()) }
        } else if (leftArg == "old") {
            { n -> operation(n.toBigInteger(), rightArg.toBigInteger()) }
        } else if (rightArg == "old") {
            { n -> operation(leftArg.toBigInteger(), n.toBigInteger()) }
        } else {
            { _ -> operation(leftArg.toBigInteger(), rightArg.toBigInteger()) }
        }
    }

    fun gcd(m: Int, n: Int): Int {
        var a = m
        var b = n
        while (b > 0) {
            val temp = b
            b = a % b
            a = temp
        }

        return a
    }

    fun lcm(m: Int, n: Int): Int = (m * n) / gcd(m, n)

    fun processInput(input: List<String>): Pair<Array<Monkey>, Int> {
        val monkeys = mutableListOf<Monkey>()
        val testValues = mutableListOf<Int>()
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
            testValues.add(testValue)
        }

        return Pair(monkeys.toTypedArray(), testValues.reduce { acc, value -> lcm(acc, value) })
    }

    fun doRound(monkeys: Array<Monkey>, spiraling: Boolean = false, lcm: Int = 1) {
        for (monkey in monkeys) {
            while (monkey.heldItems.isNotEmpty()) {
                val currItem = monkey.heldItems.removeFirst()
                monkey.inspect()

                val worryLevel = monkey.applyOperation(currItem)
                    .let { if (spiraling) { it % lcm.toBigInteger() } else { it.div(3.toBigInteger()) } }
                    .toInt()
                val monkeyToCatch = monkey.decide(worryLevel)

                monkeys[monkeyToCatch].heldItems.add(worryLevel)
            }
        }
    }

    fun multiplyTwoLargest(monkeys: Array<Monkey>): BigInteger {
        val monkeysByInspectedElements = PriorityQueue(monkeys.map { -it.inspectedItems })  // Induce max-queue

        return monkeysByInspectedElements.poll() * monkeysByInspectedElements.poll()
    }

    fun part1(input: List<String>): BigInteger {
        val (monkeys, _) = processInput(input)

        repeat(20) {
            doRound(monkeys)
        }

        return multiplyTwoLargest(monkeys)
    }

    fun part2(input: List<String>): BigInteger {
        val (monkeys, lcm) = processInput(input)

        repeat(10000) {
            doRound(monkeys, spiraling=true, lcm=lcm)
        }

        return multiplyTwoLargest(monkeys)
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}