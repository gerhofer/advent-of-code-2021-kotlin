import kotlin.math.max
import kotlin.math.min

object Day5 {

    fun part1(filePath: String) {
        val pointsCrossed = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { Path(it) }
            .flatMap { it.getPoints() }

        val pointsCrossedMultipleTimes = pointsCrossed.groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }

        println(pointsCrossedMultipleTimes.size)
    }

    fun part2(filePath: String) {
        val pointsCrossed = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { Path(it) }
            .flatMap { it.getPointsWithDiagonals() }

        val pointsCrossedMultipleTimes = pointsCrossed.groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }

        println(pointsCrossedMultipleTimes.size)
    }

}

data class Path(
    val from: Point,
    val to: Point
) {
    constructor(string: String) : this(
        Point(string.substringBefore("->")),
        Point(string.substringAfter("->"))
    )

    fun getPoints(): List<Point> {
        return if (from.col == to.col) {
            (min(from.row, to.row)..max(from.row, to.row)).map {
                Point(from.col, it)
            }
        } else if (from.row == to.row) {
            (min(from.col, to.col)..max(from.col, to.col)).map {
                Point(it, from.row)
            }
        } else {
            emptyList()
        }
    }

    fun getPointsWithDiagonals(): List<Point> {
        return if (from.col == to.col) {
            (min(from.row, to.row)..max(from.row, to.row)).map {
                Point(from.col, it)
            }
        } else if (from.row == to.row) {
            (min(from.col, to.col)..max(from.col, to.col)).map {
                Point(it, from.row)
            }
        } else {
            val xValues = getNumbersInRange(from.col, to.col)
            val yValues = getNumbersInRange(from.row, to.row)
            xValues.zip(yValues) { x, y -> Point(x, y) }
        }
    }
}

fun getNumbersInRange(from: Int, to: Int) : List<Int> {
    return if (from > to) {
        (to..from).map { it }.reversed()
    } else {
        (from..to).map { it }
    }
}

data class Point(
    val col: Int,
    val row: Int
) {
    constructor(string: String) : this(
        string.substringBefore(",").trim().toInt(),
        string.substringAfter(",").trim().toInt()
    )
}