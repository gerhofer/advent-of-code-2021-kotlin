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
        return if (from.x == to.x) {
            (min(from.y, to.y)..max(from.y, to.y)).map {
                Point(from.x, it)
            }
        } else if (from.y == to.y) {
            (min(from.x, to.x)..max(from.x, to.x)).map {
                Point(it, from.y)
            }
        } else {
            emptyList()
        }
    }

    fun getPointsWithDiagonals(): List<Point> {
        return if (from.x == to.x) {
            (min(from.y, to.y)..max(from.y, to.y)).map {
                Point(from.x, it)
            }
        } else if (from.y == to.y) {
            (min(from.x, to.x)..max(from.x, to.x)).map {
                Point(it, from.y)
            }
        } else {
            val xValues = getNumbersInRange(from.x, to.x)
            val yValues = getNumbersInRange(from.y, to.y)
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
    val x: Int,
    val y: Int
) {
    constructor(string: String) : this(
        string.substringBefore(",").trim().toInt(),
        string.substringAfter(",").trim().toInt()
    )
}