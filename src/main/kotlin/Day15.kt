object Day15 {

    fun part1(filePath: String) {
        val densities = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { line ->
                line.split("")
                    .filter { it.isNotEmpty() }
                    .map { chitonDensity ->
                        ChitonDensity(chitonDensity.toInt())
                    }
            }

        densities[0][0].optimumPath = Path(0, mutableListOf(Point(0, 0)))

        for (y in (densities.indices)) {
            for (x in (densities.indices)) {
                if (y == 0 && x == 0) {
                    continue
                }
                val fromLeft = pathFromLeft(densities, x, y)
                val fromTop = pathFromTop(densities, x, y)
                if (fromLeft != null && (fromTop == null || fromLeft.riskLevel < fromTop.riskLevel)) {
                    densities[y][x].optimumPath = fromLeft
                } else {
                    densities[y][x].optimumPath = fromTop
                }
            }
        }

        println(densities.joinToString("\n") { line ->
            line.joinToString("") {
                String.format(
                    "%4d",
                    it.optimumPath?.riskLevel ?: -1
                )
            }
        })

    }

    fun pathFromLeft(densities: List<List<ChitonDensity>>, x: Int, y: Int): Path? {
        if (x > 0) {
            val path = densities[y][x - 1].optimumPath ?: error("should not happen")
            return path.copy(
                riskLevel = path.riskLevel + densities[y][x].value,
                steps = path.steps.plus(Point(x, y)).toMutableList()
            )
        }
        return null
    }

    fun pathFromTop(densities: List<List<ChitonDensity>>, x: Int, y: Int): Path? {
        if (y > 0) {
            val path = densities[y-1][x].optimumPath ?: error("should not happen")
            return path.copy(
                riskLevel = path.riskLevel + densities[y][x].value,
                steps = path.steps.plus(Point(x, y)).toMutableList()
            )
        }
        return null
    }

    fun part2(filePath: String) {

    }

    data class Path(
        var riskLevel: Long,
        val steps: MutableList<Point>
    )

    data class ChitonDensity(
        val value: Int,
        var optimumPath: Path? = null
    )

    data class Point(
        val x: Int,
        val y: Int
    ) {

        fun left(): Point = Point(this.x - 1, this.y)
        fun top(): Point = Point(this.x, this.y - 1)
        fun right(): Point = Point(this.x + 1, this.y)
        fun bottom(): Point = Point(this.x, this.y + 1)

    }

}
