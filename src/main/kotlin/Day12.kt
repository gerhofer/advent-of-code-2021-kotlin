object Day12 {

    fun part1(filePath: String) {
        val graph = parseGraph(filePath)
        val paths = mutableListOf<CavePath>()

        for (next in graph.start.connected) {
            paths.addAll(getPaths(next, mutableListOf(graph.start)))
        }

        val validPaths = paths.filter { it.last().isEnd() }
        println(validPaths.size)
    }

    private fun getPaths(current: Node, path: CavePath): List<CavePath> {
        path.add(current)
        val paths = mutableListOf(path)
        if (!current.isEnd()) {
            for (next in current.connected.filter {
                it.letter !in path.filter { path -> !path.isBigCave() }.map { path -> path.letter }
            }) {
                paths.addAll(getPaths(next, mutableListOf<Node>().apply { this.addAll(path) }))
            }
        }
        return paths
    }

    private fun getPathsSingleVisitedTwice(current: Node, path: CavePath): List<CavePath> {
        path.add(current)
        val paths = mutableListOf(path)
        if (!current.isEnd()) {
            val smallCaves = path.filter { path -> !path.isBigCave() }
            val anySmallCaveTwice = smallCaves.groupBy { it.letter }.any { it.value.size >= 2 }
            if (anySmallCaveTwice) {
                for (next in current.connected.filter {
                    it.letter !in path.filter { path -> !path.isBigCave() }.map { path -> path.letter }
                }) {
                    paths.addAll(getPathsSingleVisitedTwice(next, mutableListOf<Node>().apply { this.addAll(path) }))
                }
            } else {
                for (next in current.connected.filter { !it.isStart() }) {
                    paths.addAll(getPathsSingleVisitedTwice(next, mutableListOf<Node>().apply { this.addAll(path) }))
                }
            }
        }
        return paths
    }

    fun part2(filePath: String) {
        val graph = parseGraph(filePath)
        val paths = mutableListOf<CavePath>()

        for (next in graph.start.connected) {
            paths.addAll(getPathsSingleVisitedTwice(next, mutableListOf(graph.start)))
        }

        val validPaths = paths.filter { it.last().isEnd() }
        println(validPaths.size)
    }

    private fun parseGraph(filePath: String): Graph {
        val connections = javaClass.getResource(filePath).readText()
            .split("\n")
            .map {
                val (from, to) = it.trim().split("-")
                Connection(from.trim(), to.trim())
            }
        val allNodes = mutableSetOf<Node>()
        for (connection in connections) {
            val from = allNodes.firstOrNull { it.letter == connection.from } ?: Node(connection.from)
            val to = allNodes.firstOrNull { it.letter == connection.to } ?: Node(connection.to)

            to.connected.add(from)
            from.connected.add(to)

            allNodes.add(from)
            allNodes.add(to)
        }

        return Graph(allNodes.firstOrNull { it.isStart() } ?: error("No start found"))
    }

}

typealias CavePath = MutableList<Node>

fun CavePath.getPath(): String =
    this.joinToString("->") { it.letter }

data class Connection(
    val from: String,
    val to: String
)

data class Graph(
    val start: Node
)

data class Node(
    val letter: String,
    val connected: MutableList<Node> = mutableListOf()
) {

    fun isStart() = "start" == letter

    fun isEnd() = "end" == letter

    fun isBigCave() = letter.all { it.isUpperCase() }

    override fun equals(other: Any?): Boolean {
        return (other != null && other is Node && other.letter == this.letter)
    }

    override fun hashCode(): Int {
        return letter.hashCode()
    }

    override fun toString(): String {
        return "Node(letter='$letter', connected=${connected.size}})"
    }


}