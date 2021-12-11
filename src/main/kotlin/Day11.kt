object Day11 {

    fun part1(filePath: String) {
        val grid = parseOctopusGrid(filePath)

        var flashes = 0
        for (day in (1..100)) {
            for (row in (0 until grid.height())) {
                for (col in (0 until grid.width())) {
                    grid.increase(Point(col, row))
                }
            }

            for (row in (0 until grid.height())) {
                for (col in (0 until grid.width())) {
                    if (grid[row][col] >= 10) {
                        grid.flash(row, col)
                    }
                }
            }

            flashes += grid.resetAfterFlash()
        }

        println(flashes)
    }

    fun part2(filePath: String) {
        val grid = parseOctopusGrid(filePath)

        var counter = 0
        while (grid.flatten().any { it != FLASHED_MARKER }) {
            grid.resetAfterFlash()

            for (row in (0 until grid.height())) {
                for (col in (0 until grid.width())) {
                    grid.increase(Point(col, row))
                }
            }

            for (row in (0 until grid.height())) {
                for (col in (0 until grid.width())) {
                    if (grid[row][col] >= 10) {
                        grid.flash(row, col)
                    }
                }
            }
            counter++
        }

        println(counter)
    }


    private fun parseOctopusGrid(filePath: String): OctopusGrid {
        return javaClass.getResource(filePath).readText()
            .split("\n")
            .map { line -> line.split("").filter { it.isNotBlank() }.map { it.toInt() }.toMutableList() }
            .toMutableList()
    }

}

typealias OctopusGrid = MutableList<MutableList<Int>>

fun OctopusGrid.width(): Int = this[0].size
fun OctopusGrid.height(): Int = this.size

fun OctopusGrid.flash(row: Int, col: Int) {
    this[row][col] = FLASHED_MARKER
    this.getNeighbours(row, col)
        .forEach {
            this.increaseAndFlash(it)
        }
}

const val FLASHED_MARKER = -1

fun OctopusGrid.increase(point: Point) {
    if (this[point.row][point.col] != FLASHED_MARKER) {
        this[point.row][point.col] = this[point.row][point.col] + 1
    }
}

fun OctopusGrid.increaseAndFlash(point: Point) {
    this.increase(point)
    if (this[point.row][point.col] >= 10) {
        this.flash(point.row, point.col)
    }
}

fun OctopusGrid.getNeighbours(row: Int, col: Int): List<Point> {
    val neighbors = mutableListOf<Point>()
    if (row > 0) { // not first row
        neighbors.add(Point(col, row - 1))
        if (col > 0) { // not first column
            neighbors.add(Point(col - 1, row - 1))
        }
        if (col < this.width() - 1) { // not last column
            neighbors.add(Point(col + 1, row - 1))
        }
    }
    if (row < this.height() - 1) { // not last row
        neighbors.add(Point(col, row + 1))
        if (col > 0) {
            neighbors.add(Point(col - 1, row + 1))
        }
        if (col < this.width() - 1) {
            neighbors.add(Point(col + 1, row + 1))
        }
    }
    if (col > 0) {
        neighbors.add(Point(col - 1, row))
    }
    if (col < this.width() - 1) {
        neighbors.add(Point(col + 1, row))
    }
    return neighbors.toList()
}

fun OctopusGrid.resetAfterFlash(): Int {
    val nines = this.flatten().count { it == FLASHED_MARKER }
    this.forEach {
        it.replaceAll { level ->
            if (level == FLASHED_MARKER) {
                0
            } else {
                level
            }
        }
    }
    return nines
}

fun OctopusGrid.print(): String {
    return this.joinToString("\n") { it.joinToString("") }
}
