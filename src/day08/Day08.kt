import java.io.File

class Matrix<T>(private val mtx: List<List<T>>) {
    fun dimensions(): Pair<Int, Int> = Pair(mtx.size, mtx[0].size)

    fun getRow(rowIdx: Int): List<T> = mtx[rowIdx]

    fun getColumn(colIdx: Int): List<T> = mtx.map { row -> row[colIdx] }

    fun forEachIndexed(f: (rowIndex: Int, columnIndex: Int, T) -> Unit) {
        this.mtx.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, item ->
                f(rowIndex, columnIndex, item)
            }
        }
    }
}

fun main() {
    fun readInput(name: String) = File("src/day08", name)
        .readLines()

    fun smallerToLeft(mtx: Matrix<Int>, thisTree: Int, pos: Pair<Int, Int>): Int {
        val row = mtx.getRow(pos.first).subList(0, pos.second)
        return row.takeLastWhile { tree -> tree < thisTree }.count()
    }

    fun smallerToRight(mtx: Matrix<Int>, thisTree: Int, pos: Pair<Int, Int>): Int {
        val row = mtx.getRow(pos.first).subList(pos.second + 1, mtx.dimensions().second)
        return row.takeWhile { tree -> tree < thisTree }.count()
    }

    fun smallerAbove(mtx: Matrix<Int>, thisTree: Int, pos: Pair<Int, Int>): Int {
        val col = mtx.getColumn(pos.second).subList(0, pos.first)
        return col.takeLastWhile { tree -> tree < thisTree }.count()
    }

    fun smallerBelow(mtx: Matrix<Int>, thisTree: Int, pos: Pair<Int, Int>): Int {
        val col = mtx.getColumn(pos.second).subList(pos.first + 1, mtx.dimensions().first)
        return col.takeWhile { tree -> tree < thisTree }.count()
    }

    fun isVisibleFromADirection(mtx: Matrix<Int>, tree: Int, pos: Pair<Int, Int>): Boolean {
        val (height, width) = mtx.dimensions()

        return ((smallerToLeft(mtx, tree, pos) == pos.second)
                or (smallerToRight(mtx, tree, pos) == width - pos.second - 1)
                or (smallerAbove(mtx, tree, pos) == pos.first)
                or (smallerBelow(mtx, tree, pos) == height - pos.first - 1))
    }

    fun calculateVisibilityScore(mtx: Matrix<Int>, tree: Int, pos: Pair<Int, Int>): Int {
        val (row, col) = pos
        val (height, width) = mtx.dimensions()

        var stl = smallerToLeft(mtx, tree, pos)
        if (stl < col) { ++stl }

        var str = smallerToRight(mtx, tree, pos)
        if (str < width - col - 1) { ++str }

        var sa = smallerAbove(mtx, tree, pos)
        if (sa < row) { ++sa }

        var sb = smallerBelow(mtx, tree, pos)
        if (sb < height - row - 1) { ++sb }

        return (stl * str * sa * sb)
    }

    fun part1(input: List<String>): Int {
        val grid = Matrix(input.map { row -> row.map(Char::digitToInt) })

        val treesSeen = mutableSetOf<Pair<Int, Int>>()
        var count = 0
        grid.forEachIndexed { rowIdx, colIdx, tree ->
            if ((Pair(rowIdx, colIdx) !in treesSeen) and isVisibleFromADirection(grid, tree, Pair(rowIdx, colIdx))) {
                count += 1
                treesSeen.add(Pair(rowIdx, colIdx))
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val grid = Matrix(input.map { row -> row.map(Char::digitToInt) })

        val treesSeen = mutableSetOf<Pair<Int, Int>>()
        var maxScoreSeenSoFar = 0
        grid.forEachIndexed { rowIdx, colIdx, tree ->
            if (Pair(rowIdx, colIdx) !in treesSeen) {
                val visibilityScore = calculateVisibilityScore(grid, tree, Pair(rowIdx, colIdx))
                if (visibilityScore > maxScoreSeenSoFar) {
                    maxScoreSeenSoFar = visibilityScore
                }

                treesSeen.add(Pair(rowIdx, colIdx))
            }
        }

        return maxScoreSeenSoFar
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}