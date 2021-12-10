object Day9 {

    fun part1(filePath: String) {
        val grid = parseGrid(filePath)

        val lowPoints = mutableListOf<Int>()
        for (row in (0..grid.size-1)) {
            for (col in (0..grid[0].size-1)) {
                val cell = grid[row][col]
                if (row > 0) {
                    val top = grid[row - 1][col]
                    if (top <= cell) {
                        continue
                    }
                }
                if (row < grid.size - 1) {
                    val bottom = grid[row + 1][col]
                    if (bottom <= cell) {
                        continue
                    }
                }
                if (col > 0) {
                    val left = grid[row][col - 1]
                    if (left <= cell) {
                        continue
                    }
                }
                if (col < grid[0].size - 1) {
                    val right = grid[row][col + 1]
                    if (right <= cell) {
                        continue
                    }
                }
                lowPoints += cell
            }
        }

        println(lowPoints.sumOf { it + 1 })
    }

    fun part2(filePath: String) {
        val grid = parseGrid(filePath)

        val lowPoints = mutableMapOf<HeightPoint, Int>()
        for (row in (0..grid.size-1)) {
            for (col in (0..grid[0].size-1)) {
                val cell = grid[row][col]
                if (row > 0) {
                    val top = grid[row - 1][col]
                    if (top <= cell) {
                        continue
                    }
                }
                if (row < grid.size - 1) {
                    val bottom = grid[row + 1][col]
                    if (bottom <= cell) {
                        continue
                    }
                }
                if (col > 0) {
                    val left = grid[row][col - 1]
                    if (left <= cell) {
                        continue
                    }
                }
                if (col < grid[0].size - 1) {
                    val right = grid[row][col + 1]
                    if (right <= cell) {
                        continue
                    }
                }
                lowPoints[HeightPoint(cell, row, col)] = getBasinSize(grid, row, col, mutableSetOf()).size
            }
        }

        val result = lowPoints.values.toList().sortedDescending().take(3)
            .foldRight(1) { a, b -> a * b }

        println(lowPoints)

        println(result)
    }

    fun getBasinSize(grid: Grid, row: Int, col: Int, neighbors: MutableSet<HeightPoint>) : Set<HeightPoint> {
        if (row > 0) {
            val topPoint = HeightPoint(grid[row - 1][col], row - 1, col)
            if (topPoint.point != 9 && !neighbors.contains(topPoint)) {
                neighbors.add(topPoint)
                getBasinSize(grid, row - 1, col, neighbors)
            }
        }
        if (row < grid.size - 1) {
            val bottomPoint = HeightPoint(grid[row + 1][col], row + 1, col)
            if (bottomPoint.point != 9 && !neighbors.contains(bottomPoint)) {
                neighbors.add(bottomPoint)
                getBasinSize(grid, row + 1, col, neighbors)
            }
        }
        if (col > 0) {
            val leftPoint = HeightPoint(grid[row][col - 1], row, col - 1)
            if (leftPoint.point!= 9 && !neighbors.contains(leftPoint)) {
                neighbors.add(leftPoint)
                getBasinSize(grid, row, col - 1, neighbors)
            }
        }
        if (col < grid[0].size - 1) {
            val rightPoint = HeightPoint(grid[row][col + 1], row, col + 1)
            if (rightPoint.point != 9 && !neighbors.contains(rightPoint)) {
                neighbors.add(rightPoint)
                getBasinSize(grid, row, col + 1, neighbors)
            }
        }

       return neighbors.toSet()
    }

    fun parseGrid(filePath: String): Grid {
        return javaClass.getResource(filePath).readText()
            .split("\n")
            .map { line -> line.split("").filter { it.isNotBlank() }.map { it.toInt() } }
    }

}

typealias Grid = List<List<Int>>

data class HeightPoint(
    val point: Int,
    val row: Int,
    val col: Int
)