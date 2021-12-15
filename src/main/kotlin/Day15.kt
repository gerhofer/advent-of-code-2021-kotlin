object Day15 {

    fun part1(filePath: String) {
        val densities = parseDensities(filePath)
        SIZE = densities.size

        densities[0][0].optimumPath = Path(0, mutableListOf(Point(0, 0)))

        for (y in (densities.indices)) {
            for (x in (densities.indices)) {
                if (y == 0 && x == 0) {
                    continue
                }
                val fromLeft = pathFromLeft(densities, x, y)
                val fromTop = pathFromTop(densities, x, y)
                val shouldChooseLeft = fromLeft != null && (fromTop == null || fromLeft.riskLevel < fromTop.riskLevel)
                if (shouldChooseLeft) {
                    densities[y][x].optimumPath = fromLeft
                    checkIfTopCanBeReachedViaBottom(y, densities, x, fromTop, fromLeft)
                } else {
                    densities[y][x].optimumPath = fromTop
                    checkIfLeftCanBeReachedViaRight(x, densities, y, fromTop)
                }
            }
        }

        println(densitiesToString(densities))
    }

    private fun densitiesToString(densities: List<List<ChitonDensity>>) =
        densities.joinToString("\n") { line ->
            line.joinToString("") {
                String.format(
                    "%4d",
                    it.optimumPath?.riskLevel ?: -1
                )
            }
        }

    fun updateWithNewPath(densities: List<List<ChitonDensity>>, x: Int, y: Int, newPath: Path) {
        val newOptimumPath = newPath.copy(
            riskLevel = newPath.riskLevel + densities[y][x].value,
            steps = newPath.steps.plus(Point(x, y)).toMutableList()
        )
        densities[y][x].optimumPath = newOptimumPath
        // check neighbours and update them with new path if this is a shorter path
        for (neighbour in Point(x, y).getNeighbours().filter { it !in newOptimumPath.steps }) {
            val neighbourDensity = densities[neighbour.y][neighbour.x]
            val neighbourPathSoFar = neighbourDensity.optimumPath
            if (neighbourPathSoFar != null && neighbourPathSoFar.riskLevel > (newOptimumPath.riskLevel + neighbourDensity.value)) {
                updateWithNewPath(densities, neighbour.x, neighbour.y, newOptimumPath)
            }
        }

    }

    private fun pathFromLeft(densities: List<List<ChitonDensity>>, x: Int, y: Int): Path? {
        if (x > 0) {
            val path = densities[y][x - 1].optimumPath ?: error("should not happen")
            return path.copy(
                riskLevel = path.riskLevel + densities[y][x].value,
                steps = path.steps.plus(Point(x, y)).toMutableList()
            )
        }
        return null
    }

    private fun pathFromTop(densities: List<List<ChitonDensity>>, x: Int, y: Int): Path? {
        if (y > 0) {
            val path = densities[y - 1][x].optimumPath ?: error("should not happen")
            return path.copy(
                riskLevel = path.riskLevel + densities[y][x].value,
                steps = path.steps.plus(Point(x, y)).toMutableList()
            )
        }
        return null
    }

    fun part2(filePath: String) {
        val densities = parseDensities(filePath)
        val originalSize = densities.size
        SIZE = densities.size * 5

        val bigDensities = createIncresedDensities(originalSize, densities)
        bigDensities[0][0].optimumPath = Path(0, mutableListOf(Point(0, 0)))

        for (y in (bigDensities.indices)) {
            for (x in (bigDensities.indices)) {
                if (y == 0 && x == 0) {
                    continue
                }
                val fromLeft = pathFromLeft(bigDensities, x, y)
                val fromTop = pathFromTop(bigDensities, x, y)
                val shouldChooseLeft = fromLeft != null && (fromTop == null || fromLeft.riskLevel < fromTop.riskLevel)
                if (shouldChooseLeft) {
                    bigDensities[y][x].optimumPath = fromLeft
                    checkIfTopCanBeReachedViaBottom(y, bigDensities, x, fromTop, fromLeft)
                } else {
                    bigDensities[y][x].optimumPath = fromTop
                    checkIfLeftCanBeReachedViaRight(x, bigDensities, y, fromTop)
                }
            }
        }

        println(densitiesToString(bigDensities))
    }

    private fun createIncresedDensities(
        originalSize: Int,
        densities: List<MutableList<ChitonDensity>>
    ): MutableList<MutableList<ChitonDensity>> {
        val bigDensities = MutableList(SIZE) { MutableList(SIZE) { ChitonDensity(0) } }

        for (y in (0 until SIZE)) {
            val rowModifier = y / originalSize
            for (x in (0 until SIZE)) {
                val colModifier = x / originalSize
                val increasedVal = densities[y % originalSize][x % originalSize].value + rowModifier + colModifier
                val restByNine = increasedVal % 9
                bigDensities[y][x].value = if (restByNine == 0) {
                    9
                } else {
                    restByNine
                }
            }
        }
        return bigDensities
    }

    private fun parseDensities(filePath: String) = javaClass.getResource(filePath).readText()
        .split("\r\n")
        .map { line ->
            line.split("")
                .filter { it.isNotEmpty() }
                .map { chitonDensity ->
                    ChitonDensity(chitonDensity.toInt())
                }.toMutableList()
        }

    private fun checkIfLeftCanBeReachedViaRight(
        x: Int,
        bigDensities: MutableList<MutableList<ChitonDensity>>,
        y: Int,
        fromTop: Path?
    ) {
        if (x > 0) {
            val originalLeft = bigDensities[y][x - 1]
            if (originalLeft.optimumPath != null && fromTop != null && originalLeft.optimumPath!!.riskLevel > (fromTop.riskLevel + originalLeft.value)) {
                updateWithNewPath(bigDensities, x - 1, y, fromTop)
            }
        }
    }

    private fun checkIfTopCanBeReachedViaBottom(
        y: Int,
        bigDensities: MutableList<MutableList<ChitonDensity>>,
        x: Int,
        fromTop: Path?,
        fromLeft: Path?
    ) {
        if (y > 0) {
            val originalTop = bigDensities[y - 1][x]
            if (originalTop.optimumPath != null && fromTop != null && originalTop.optimumPath!!.riskLevel > (fromLeft.riskLevel + originalTop.value)) {
                updateWithNewPath(bigDensities, x, y - 1, fromLeft)
            }
        }
    }

    data class Path(
        var riskLevel: Long,
        val steps: MutableList<Point>
    )

    data class ChitonDensity(
        var value: Int,
        var optimumPath: Path? = null
    )

    data class Point(
        val x: Int,
        val y: Int
    ) {

        private fun left(): Point = Point(this.x - 1, this.y)
        private fun top(): Point = Point(this.x, this.y - 1)
        private fun right(): Point = Point(this.x + 1, this.y)
        private fun bottom(): Point = Point(this.x, this.y + 1)

        fun getNeighbours(): List<Point> = listOfNotNull(
            if (x > 0) {
                left()
            } else {
                null
            },
            if (x < SIZE - 1) {
                right()
            } else {
                null
            },
            if (y > 0) {
                top()
            } else {
                null
            },
            if (y < SIZE - 1) {
                bottom()
            } else {
                null
            }
        )
    }

    var SIZE = 100

}
