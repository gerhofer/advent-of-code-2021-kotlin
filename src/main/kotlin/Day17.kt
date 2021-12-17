import kotlin.math.abs

object Day17 {

    fun part1(filePath: String) {
        val (_, yTargetRange) = parseTargetArea(filePath)
        val maxReachedY = findMaxReachedY(yTargetRange)
        println(maxReachedY)
    }

    fun findMaxReachedY(target: Range) : Int {
        val to = findMaxY(target)
        return (1..to).sum()
    }

    private fun findMaxY(target: Range) = abs(target.from) - 1

    fun part2(filePath: String) {
        val (xTargetRange, yTargetRange) = parseTargetArea(filePath)

        val minX = finMinX(xTargetRange)
        val targetHeight = yTargetRange.to - yTargetRange.from

        val indirectSteps = (minX..xTargetRange.to - targetHeight).map { x ->
            findApplicableYs(x, xTargetRange, yTargetRange).map { y ->
                Step(x, y)
            }
        }.flatten().distinct()

        val directSteps = (xTargetRange.from..xTargetRange.to).map { x ->
            (yTargetRange.from..yTargetRange.to).map { y ->
                Step(x, y)
            }
        }.flatten()

        println(indirectSteps)
        println((directSteps + indirectSteps).distinct().size)

    }

    private fun parseTargetArea(filePath: String) = javaClass.getResource(filePath).readText()
        .drop("target area: x=".length)
        .split(", y=")
        .map {
            val (from, to) = it.split("..").map { value -> value.toInt() }
            Range(from, to)
        }

    fun findApplicableYs (xStep: Int, targetX: Range, targetY: Range) : List<Int> {
        val ySteps = mutableListOf<Int>()
        val maxY = findMaxY(targetY)

        loop@ for (yStep in (targetY.to+1..maxY)) {
            var x = 0
            var y = 0
            var currentXStep = xStep
            var currentYStep = yStep

            while(x <= targetX.to && y >= targetY.from) {
                x += currentXStep
                y += currentYStep
                currentYStep -= 1
                currentXStep = if (currentXStep == 0) { 0 } else { currentXStep - 1 }

                if (x in (targetX.from..targetX.to) && y in (targetY.from..targetY.to)) {
                    ySteps.add(yStep)
                    continue@loop
                }
            }
        }

        return ySteps
    }

    fun finMinX(target: Range) : Int {
        var sum = 0
        var i = 0
        while (sum < target.from) {
            i++
            sum += i
        }
        return i
    }

    data class Range(
        val from: Int,
        val to: Int
    )

    data class Step(
        val x: Int,
        val y: Int
    )
}