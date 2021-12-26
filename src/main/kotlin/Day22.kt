import kotlin.math.max
import kotlin.math.min

object Day22 {

    fun part1(filePath: String) {
        val instructions = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { parseInstruction(it) }
            .filter {
                it.volume.x.first >= -50 && it.volume.x.last <= 50 &&
                        it.volume.y.first >= -50 && it.volume.y.last <= 50 &&
                        it.volume.z.first >= -50 && it.volume.y.last <= 50
            }

        var turnedOn = mutableSetOf<Point>()
        for (instruction in instructions) {
            if (instruction.direction == Direction.ON) {
                turnedOn += getPointsInRange(instruction)
            } else {
                turnedOn.removeAll {
                    it.x in instruction.volume.x && it.y in instruction.volume.y && it.z in instruction.volume.z
                }
            }
        }

        println(turnedOn.size)
    }

    fun getPointsInRange(instruction: Instruction): Set<Point> {
        val points = mutableSetOf<Point>()
        for (x in instruction.volume.x) {
            for (y in instruction.volume.y) {
                for (z in instruction.volume.z) {
                    points.add(Point(x, y, z))
                }
            }
        }
        return points.toSet()
    }

    fun part2(filePath: String) {
        val instructions = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { parseInstruction(it) }

        var turnedOn = mutableSetOf<Volume>()
        for (instruction in instructions) {
            if (instruction.direction == Direction.ON) {
                val lightVolumes = mutableSetOf<Volume>()
                for (existingArea in turnedOn) {
                    val remainders = existingArea - instruction.volume
                    lightVolumes += remainders
                }
                lightVolumes += instruction.volume
                turnedOn = lightVolumes
                //println("After adding we have the volumes: $turnedOn")
                //println("with a sum of: ${turnedOn.sumOf { it.count() }}")

            } else {
                val lightVolumes = mutableSetOf<Volume>()
                for (existingArea in turnedOn) {
                    val remainders = existingArea - instruction.volume
                    lightVolumes += remainders
                }
                turnedOn = lightVolumes
                //println("After removing we have the volumes: $turnedOn")
                //println("with a sum of: ${turnedOn.sumOf { it.count() }}")
            }
        }

        //println(turnedOn)
        println(turnedOn.sumOf { it.count() })
    }


    fun parseInstruction(line: String): Instruction {
        val direction = if (line.startsWith("on")) Direction.ON else Direction.OFF
        val (x, y, z) = line.substringAfter(" ").split(",")
        return Instruction(
            direction,
            x.substringBefore("..").substringAfter("=").trim().toLong()..x.substringAfter("..").trim().toLong(),
            y.substringBefore("..").substringAfter("=").trim().toLong()..y.substringAfter("..").trim().toLong(),
            z.substringBefore("..").substringAfter("=").trim().toLong()..z.substringAfter("..").trim().toLong()
        )
    }

    data class Volume(
        val x: LongRange,
        val y: LongRange,
        val z: LongRange
    ) {

        fun contains(other: Volume): Boolean {
            return (other.x.first in x || other.x.last in x) &&
                    (other.y.first in y || other.y.last in y) &&
                    (other.z.first in z || other.z.last in z)
        }

        operator fun minus(other: Volume): List<Volume> {
            val remainders = mutableListOf<Volume>()

            val fromZ = max(z.first, other.z.last + 1)
            if (fromZ <= z.last) {
                // slice above
                remainders.add(Volume(x, y, fromZ..z.last))
            }

            val toX = min(z.last, other.z.first - 1)
            if (z.first <= toX) {
                // slice below
                remainders.add(Volume(x, y, z.first..toX))
            }

            val sliceZRange = max(other.z.first, z.first)..min(other.z.last, z.last)
            if (sliceZRange.count() > 0) {
                val fromX = max(x.first, other.x.last + 1)
                if (fromX <= x.last) {
                    // slice to the right
                    remainders.add(Volume(fromX..x.last, y, sliceZRange))
                }

                val toX = min(x.last, other.x.first - 1)
                if (x.first <= toX) {
                    // slice to the left
                    remainders.add(Volume(x.first..toX, y, sliceZRange))
                }

                val sliceXRange = max(other.x.first, x.first)..min(other.x.last, x.last)
                if (sliceXRange.count() > 0) {
                    val fromY = max(y.first, other.y.last + 1)
                    if (fromY <= y.last) {
                        // slice to the back
                        remainders.add(Volume(sliceXRange, fromY..y.last, sliceZRange))
                    }

                    val toY = min(y.last, other.y.first - 1)
                    if (y.first <= toY) {
                        // slice to the front
                        remainders.add(Volume(sliceXRange, y.first..toY, sliceZRange))
                    }
                }
            }

            return remainders.toList()
        }

        fun count() = x.count().toLong() * y.count().toLong() * z.count().toLong()

    }

    data class Instruction(
        val direction: Direction,
        val volume: Volume
    ) {
        constructor(direction: Direction, x: LongRange, y: LongRange, z: LongRange) : this(direction, Volume(x, y, z))
    }

    enum class Direction {
        ON,
        OFF
    }

    data class Point(
        val x: Long,
        val y: Long,
        val z: Long
    )
}