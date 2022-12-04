import java.io.File

fun main() {
    fun readInput(name: String) = File("src/day03", name)
        .readLines()

    fun getItemPriority(item: Char): Int =
        if (item.isLowerCase()) {
            item.code - 96
        } else {
            item.code - 38
        }

    fun part1(input: List<String>): Int {
        var sumOfPriorities = 0
        for (line in input) {
            val numItemsInFirstRucksack = line.length / 2

            val itemsInFirstRucksack = line.take(numItemsInFirstRucksack).toSet()
            val seenInSecondRucksack = mutableSetOf<Char>()
            for (item in line.takeLast(numItemsInFirstRucksack)) {
                if (itemsInFirstRucksack.contains(item) and !seenInSecondRucksack.contains(item)) {
                    sumOfPriorities += getItemPriority(item)

                    seenInSecondRucksack.add(item)
                }
            }
        }

        return sumOfPriorities
    }

    fun part2(input: List<String>): Int {
        var sumOfPriorities = 0
        var group = arrayListOf<Set<Char>>()
        for (line in input) {
            group.add(line.toSet())

            if (group.size == 3) {
                val itemsCommonToAllRucksacks = group.reduce { acc, currRucksack -> acc.intersect(currRucksack) }
                val item = itemsCommonToAllRucksacks.first()
                sumOfPriorities += getItemPriority(item)

                group = arrayListOf()
            }
        }

        return sumOfPriorities
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}
