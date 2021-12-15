object Day15 {

    fun part1(filePath: String) {
        val chitonDensities = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { line -> line.split("")
                .filter { it.isNotEmpty() }
                .map { chitonDensity -> chitonDensity.toInt() }
            }

        val paths = mutableListOf<Path>()
        val pathWithStartingPoint = Path(
            riskLevel = 0,
            steps = mutableListOf(Point(0, 0))
        )

        getShortestPath(chitonDensities, pathWithStartingPoint, paths)

        println(paths.minOf { it.riskLevel })
    }

    fun getShortestPath(area: List<List<Int>>, currentPath: Path, existingPaths: MutableList<Path>) {
        val last = currentPath.steps.last()
        if (last.x == 0 && last.y == 0) {
            println("added path $currentPath")
            existingPaths.add(currentPath)
            return
        }

        if (existingPaths.any { it.riskLevel <= currentPath.riskLevel }) {
            return
        }

        val neighbours = getPossibleNeighbours(area, currentPath.steps.last())
            .filter { it !in currentPath.steps }

        for (neighbour in neighbours.sortedBy { area[it.y][it.x] }) {
            val newCurrentPath = currentPath.copy(
                riskLevel = currentPath.riskLevel + area[neighbour.y][neighbour.x],
                steps = currentPath.steps.plus(neighbour).toMutableList()
            )

            getShortestPath(area, newCurrentPath, existingPaths)
        }

    }

    fun getPossibleNeighbours(area: List<List<Int>>, currentPoint: Point) : List<Point> {
        val neighbors = mutableListOf<Point>()
        if (currentPoint.x > 0) {
            neighbors.add(currentPoint.left())
        }

        //if (currentPoint.x < area.size - 1) {
        //    neighbors.add(currentPoint.right())
        //}

        if (currentPoint.y > 0) {
            neighbors.add(currentPoint.top())
        }

        //if (currentPoint.y < area.size - 1) {
        //    neighbors.add(currentPoint.bottom())
        //}
        return neighbors.toList()
    }

    fun part2(filePath: String) {

    }

    data class Path(
        var riskLevel: Long,
        val steps: MutableList<Point>
    )

    data class ChitonDensity(
        val value: Int,
        var excluded: Boolean = false
    )

    data class Point(
        val x: Int,
        val y: Int
    ) {

        fun left(): Point = Point(this.x - 1, this.y)
        fun top(): Point = Point(this.x, this.y - 1)
        fun right(): Point = Point(this.x + 1, this.y)
        fun bottom(): Point = Point(this.x , this.y + 1)

    }

}
