package org.softwarecurmudgeon.solvers

import org.softwarecurmudgeon.common.Day

sealed interface DirectoryTraversalLine {
    companion object {
        fun parse(line: String): DirectoryTraversalLine =
            when(line.first()) {
                '$' -> parseCommand(line.drop(2))
                'd' -> parseDirectory(line.drop(1))
                else -> parseFile(line)
            }

        private fun parseFile(line: String): DirectoryTraversalLine {
            val (number, name) = line.split(" ")
            return FileResultLine(
                name = name,
                size = number.toInt()
            )
        }

        private fun parseDirectory(line: String): DirectoryTraversalLine =
            DirectoryResultLine(line.split(" ")[1])

        private fun parseCommand(line: String): DirectoryTraversalLine =
            when(line.take(2)) {
                "cd" -> Cd(line.split(" ")[1])
                "ls" -> Ls
                else -> throw IllegalArgumentException("Invalid command")
            }
    }
}

data class Cd(val target: String): DirectoryTraversalLine
object Ls: DirectoryTraversalLine
data class DirectoryResultLine(val name: String): DirectoryTraversalLine
data class FileResultLine(val name: String, val size: Int): DirectoryTraversalLine

sealed interface Node

data class DirectoryNode(
    val name: String,
    val containsFiles: List<FileNode> = emptyList(),
    val containsDirectories: List<String> = emptyList(),
): Node {
    fun size(traversalState: TraversalState): Int {
        return containsFiles.sumOf { it.size } + containsDirectories.sumOf { traversalState.directorySize(it) }
    }
}

data class FileNode(val name: String, val size: Int): Node

class TraversalState(
    val currentPath: MutableList<String> = mutableListOf(),
    val directories: MutableList<DirectoryNode> = mutableListOf(),
    var currentDirectoryNode: DirectoryNode? = null,
) {
    fun directorySizes(): List<Int> = directories.map {
        it.size(this)
    }

    fun directorySize(directoryName: String): Int = directories.first { it.name == directoryName }.size(this)
}
object TwentyTwoDaySeven: Solution<DirectoryTraversalLine, Int>(), Solver {
    override val day: Day
        get() = Day(year = 2022, day = 7)

    override fun parseInput(input: Sequence<String>): Sequence<DirectoryTraversalLine> =
        input
            .filter(String::isNotEmpty)
            .map(DirectoryTraversalLine::parse)

    private fun buildState(input: Sequence<DirectoryTraversalLine>): TraversalState {
        return input.plus(Cd("..")).fold(TraversalState()) { state, step ->
            when(step) {
                is Cd -> {
                    when (step.target) {
                        ".." -> {
                            state.currentPath.removeLast()
                        }
                        else -> {
                            state.currentPath += step.target
                        }
                    }
                    state.currentDirectoryNode?.let {
                        state.directories += it
                        state.currentDirectoryNode = null
                    }

                    state
                }
                is Ls -> {
                    state.currentDirectoryNode = DirectoryNode(name = state.currentPath.joinToString("/"))
                    state
                }
                is DirectoryResultLine -> {
                    val currentDirectoryNode = state.currentDirectoryNode!!
                    state.currentDirectoryNode = currentDirectoryNode.copy(
                        containsDirectories = currentDirectoryNode.containsDirectories.plus(state.currentPath.plus(step.name).joinToString("/"))
                    )
                    state
                }
                is FileResultLine -> {
                    val currentDirectoryNode = state.currentDirectoryNode!!
                    state.currentDirectoryNode = currentDirectoryNode.copy(
                        containsFiles = currentDirectoryNode.containsFiles.plus(FileNode(name = step.name, size = step.size))
                    )
                    state
                }
            }
        }
    }

    override fun partOne(input: Sequence<DirectoryTraversalLine>): Int {
        val state = buildState(input)
        return state.directorySizes().filter { it < 100000}.sum()
    }


    override fun partTwo(input: Sequence<DirectoryTraversalLine>): Int {
        val state = buildState(input)
        println(state.directories)
        val rootSize = state.directorySize("/")
        val target = 30000000 - (70000000 - rootSize)
        return state.directories.map {
            state.directorySize(it.name)
        }
            .filter { it > target }
            .sorted().first()
    }

}
