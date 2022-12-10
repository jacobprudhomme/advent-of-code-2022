import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.min
import kotlin.properties.Delegates

sealed class FileSystem(val name: String) {
    lateinit var parent: Directory
    abstract var size: Int

    fun setFSParent(parent: Directory) {
        this.parent = parent
    }
}
class Directory(name: String = "/"): FileSystem(name) {
    private val children: ArrayList<FileSystem> = arrayListOf()
    override var size by Delegates.observable(0) {
        _, oldSize, newSize -> if (parent !== this) {
            parent.recalculateSize(newSize - oldSize)
        }
    }

    fun addChild(child: FileSystem) {
        child.setFSParent(this)
        children.add(child)
        size += child.size
    }

    fun sumDirsLessThan(size: Int): Int =
        children.fold(if (this.size < size) { this.size } else { 0 }) { acc, dir ->
            when (dir) {
                is Directory -> acc + dir.sumDirsLessThan(size)
                else         -> acc
            }
        }

    fun findSmallestDirGreaterThan(size: Int): Int =
        children.fold(if (this.size < size) { Int.MAX_VALUE } else { this.size }) { acc, dir ->
            when (dir) {
                is Directory -> min(acc, dir.findSmallestDirGreaterThan(size))
                else         -> acc
            }
        }

    private fun recalculateSize(delta: Int) {
        size += delta
    }
}
class File(name: String, override var size: Int): FileSystem(name)

fun main() {
    fun readInput(name: String) = File("src/day07", name)
        .readLines()

    fun executeCd(currDir: Directory, nextInput: List<String>, nextDirName: String): Pair<Directory, List<String>> {
        val nextDir = if (nextDirName == "..") {
            currDir.parent
        } else {
            val dir = Directory(nextDirName)
            currDir.addChild(dir)
            dir
        }

        return Pair(nextDir, nextInput)
    }

    fun executeLs(currDir: Directory, nextInput: List<String>): Pair<Directory, List<String>> {
        val output = nextInput.takeWhile { !it.startsWith("$ ") }
        val rest = nextInput.dropWhile { !it.startsWith("$ ") }

        for (fileOrDir in output) {
            val (first, name, _) = fileOrDir.split(" ")
            if (first == "dir") {
                val dir = Directory(name)
                currDir.addChild(dir)
            } else {
                val size = first.toInt()
                val file = File(name, size)
                currDir.addChild(file)
            }
        }

        return Pair(currDir, rest)
    }

    fun executeCommand(currDir: Directory, input: List<String>): Pair<Directory, List<String>> {
        val line = input.first()
        val rest = input.drop(1)

        return if (line.startsWith("$ ")) {
            when (line.slice(2..3)) {
                "cd" -> executeCd(currDir, rest, line.drop(5))
                "ls" -> executeLs(currDir, rest)
                else -> throw IllegalArgumentException("We should never get here")
            }
        } else {
            throw IllegalArgumentException("We should never get here")
        }
    }

    fun processInput(input: List<String>): Directory {
        val root = Directory()
        root.setFSParent(root)

        var currDir = root
        var inputLeft = input.toList()

        while (inputLeft.isNotEmpty()) {
            val (nextDir, nextInput) = executeCommand(currDir, inputLeft)
            currDir = nextDir
            inputLeft = nextInput
        }

        return root
    }

    fun part1(input: List<String>): Int {
        val SIZE_TARGET = 100000

        val fileSystem = processInput(input)
        return fileSystem.sumDirsLessThan(SIZE_TARGET)
    }

    fun part2(input: List<String>): Int {
        val TOTAL_DISK_SIZE = 70000000
        val UPDATE_SIZE     = 30000000

        val fileSystem = processInput(input)
        val unusedSpace = TOTAL_DISK_SIZE - fileSystem.size
        val spaceNeededToUpdate = UPDATE_SIZE - unusedSpace
        return fileSystem.findSmallestDirGreaterThan(spaceNeededToUpdate)
    }

    val input = readInput("input")
    println(part1(input))
    println(part2(input))
}