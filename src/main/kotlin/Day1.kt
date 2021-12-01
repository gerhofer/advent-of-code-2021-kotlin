object Day1 {

    fun part1(filePath: String) {
        val numbers = javaClass.getResource(filePath).readText()
            .split("\n")
            .map { line -> line.trim().toInt() }

        val increases = numbers
            .filterIndexed { idx, current -> (idx + 1) < numbers.size && current < numbers[idx+1]}
            .count()

        println(increases)
    }

    fun part2(filePath: String) {
        val numbers = javaClass.getResource(filePath).readText()
            .split("\n")
            .map { line -> line.trim().toInt() }

        val increases = numbers
            .filterIndexed { idx, _ -> (idx + 3) < numbers.size && numbers.sumOfThree(idx) < numbers.sumOfThree(idx+1)}
            .count()

        println(increases)
    }

}

fun List<Int>.sumOfThree(start: Int): Int {
    return this[start] + this[start + 1] + this[start + 2]
}