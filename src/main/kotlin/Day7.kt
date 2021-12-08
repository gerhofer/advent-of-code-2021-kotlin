import kotlin.math.abs

object Day7 {

    fun part1(filePath: String) {
        val crabPositions = javaClass.getResource(filePath).readText()
            .split(",")
            .map { it.trim().toInt() }
            .toMutableList()

        val minimum = crabPositions.minOrNull() ?: Int.MIN_VALUE
        val maximum = crabPositions.maxOrNull() ?: Int.MAX_VALUE

        val positionsToFuelCosts = (minimum..maximum).associateWith {
            crabPositions.sumOf { crabPosition -> abs(crabPosition - it) }
        }

        println(crabPositions.average())

        val cheapestWay = positionsToFuelCosts.minByOrNull { it.value }
        println("${cheapestWay?.key} costs ${cheapestWay?.value}")
    }

    fun part2(filePath: String) {
        val crabPositions = javaClass.getResource(filePath).readText()
            .split(",")
            .map { it.trim().toInt() }
            .toMutableList()

        val minimum = crabPositions.minOrNull() ?: Int.MIN_VALUE
        val maximum = crabPositions.maxOrNull() ?: Int.MAX_VALUE

        val positionsToFuelCosts = (minimum..maximum).associateWith {
            crabPositions.sumOf { crabPosition -> (0..abs(crabPosition - it)).sum() }
        }

        val cheapestWay = positionsToFuelCosts.minByOrNull { it.value }
        println("${cheapestWay?.key} costs ${cheapestWay?.value}")
    }

}