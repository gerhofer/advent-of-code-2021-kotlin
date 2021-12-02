object Day2 {

    fun part1(filePath: String) {
        val lines = javaClass.getResource(filePath).readText()
            .split("\n")

        var depth = 0
        var horizontalPosition = 0
        for (line in lines) {
            val (direction, value) = line.trim().split(" ")
            when (direction) {
                "forward" -> horizontalPosition += value.toInt()
                "down" -> depth += value.toInt()
                "up" -> depth -= value.toInt()
            }
        }

        println(depth * horizontalPosition)
    }

    fun part2(filePath: String) {
        val lines = javaClass.getResource(filePath).readText()
            .split("\n")

        var aim = 0
        var horizontalPosition = 0
        var depth = 0
        for (line in lines) {
            val (direction, value) = line.trim().split(" ")
            when (direction) {
                "forward" -> {
                    horizontalPosition += value.toInt()
                    depth += aim * value.toInt()
                }
                "down" -> aim += value.toInt()
                "up" -> aim -= value.toInt()
            }
        }

        println(depth * horizontalPosition)
    }

}