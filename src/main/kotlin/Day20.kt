object Day20 {

    fun part1(filePath: String) {
        val (algorithm, pattern) = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        val image = mapImage(pattern)
        val enhancedImage = enhanceImage(algorithm, image, false)
        val twiceEnhancedImage = enhanceImage(algorithm, enhancedImage, algorithm[0] == '#')

        println(twiceEnhancedImage.flatten().count { it })
    }

    fun part2(filePath: String) {
        val (algorithm, pattern) = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        var image = mapImage(pattern)

        for (i in (1..50)) {
            val enhancedImage = enhanceImage(algorithm, image, i % 2 == 0 && algorithm[0] == '#')
            image = enhancedImage
        }

        println(image.flatten().count { it })
    }

    fun printImage(image: List<List<Boolean>>) {
        println()
        println(
            image.joinToString("\n") { row ->
                row.joinToString("") { if (it) "#" else "." }
            })
    }

    fun enhanceImage(algorithm: String, image: List<List<Boolean>>, default: Boolean): List<List<Boolean>> {
        val enhancedImage = mutableListOf<List<Boolean>>()
        for (y in (-1..image.size + 1)) {
            val row = mutableListOf<Boolean>()
            for (x in -1..image[0].size + 1) {
                val index = getValue(image, Point(x, y), default)
                row.add(algorithm[index] == '#')
            }
            enhancedImage.add(row.toList())
        }
        return enhancedImage.toList()
    }

    fun getValue(pattern: List<List<Boolean>>, point: Point, default: Boolean): Int {
        var binaryRepresenation = ""
        for (yMod in (-1..1)) {
            for (xMod in (-1..1)) {
                val x = point.x + xMod
                val y = point.y + yMod
                val currentLight = if (x in pattern.indices && y in pattern[0].indices) {
                    pattern[y][x]
                } else {
                    default
                }
                binaryRepresenation += if (currentLight) "1" else "0"
            }
        }
        return binaryRepresenation.toInt(2)
    }

    fun mapImage(pattern: String): List<List<Boolean>> {
        return pattern.split("\r\n")
            .map { line -> line.split("").filter { it.isNotEmpty() }.map { it == "#" } }
    }

    fun mapPattern(pattern: String): Map<Point, Boolean> {
        val linesInPattern = pattern.split("\r\n")
        val inputImage = mutableMapOf<Point, Boolean>()

        for (y in linesInPattern.indices) {
            for (x in linesInPattern[y].indices) {
                inputImage[Point(x, y)] = linesInPattern[y][x] == '#'
            }
        }

        return inputImage.toMap()
    }

    data class Point(
        val x: Int,
        val y: Int
    )
}