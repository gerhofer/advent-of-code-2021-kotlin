import java.io.File

object Day13 {

    fun part1(filePath: String) {
        val (paper, instructions) = parseInput(filePath)

        paper.printToFile("unfolded.txt")

        val firstInstruction = instructions.first()

        val foldedPaper = if (firstInstruction.direction == Direction.HORIZONTAL) {
            foldHorizontally(paper, firstInstruction.axis)
        } else {
            foldVertically(paper, firstInstruction.axis)
        }

        foldedPaper.printToFile("twice-folded.txt")

        println(foldedPaper.flatten().count { it })
    }

    fun part2(filePath: String) {
        val (paper, instructions) = parseInput(filePath)

        paper.printToFile("unfolded.txt")

        var paperInFolding = paper

        for (instruction in instructions) {
            paperInFolding = if (instruction.direction == Direction.HORIZONTAL) {
                foldHorizontally(paperInFolding, instruction.axis)
            } else {
                foldVertically(paperInFolding, instruction.axis)
            }
        }

        paperInFolding.print()
    }

    private fun parseInput(filePath: String) : Input {
        val (dots, instructions) = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        val grid = parseAndInitGrid(dots)
        val foldInstructions = parseFoldInstructions(instructions)

        return Input(grid, foldInstructions)
    }

    private fun parseAndInitGrid(dots: String): Array<Array<Boolean>> {
        val markedDots = dots.split("\r\n")
            .map { it.split(",").map { number -> number.trim().toInt() } }
        val xValues = markedDots.map { it[0] }
        val yValues = markedDots.map { it[1] }

        println("x: ${xValues.maxOrNull()}")
        println("y: ${yValues.maxOrNull()}")

        val grid = Array((yValues.maxOrNull()  ?: 0) + 1) { Array((xValues.maxOrNull() ?: 0) + 1) { false } }

        xValues.zip(yValues).forEach { (x, y) -> grid[y][x] = true }

        return grid
    }

    private fun parseFoldInstructions(instructions: String): List<FoldInstruction> {
        val foldInstructions = instructions.split("\r\n").map {
            FoldInstruction(
                if (it.contains("x")) {
                    Direction.VERTICAL
                } else {
                    Direction.HORIZONTAL
                },
                it.substringAfter("=").toInt()
            )
        }
        return foldInstructions
    }

    private fun foldVertically(paper: TransparentPaper, xValue: Int) : TransparentPaper {
        val newPaper = Array(paper.size) { Array(xValue) { false } }
        val numberOfColumns = if (paper[0].size % 2 != 0) { paper[0].size - 1 } else { paper[0].size }

        for (row in newPaper.indices) {
            for (column in (newPaper[row].indices)) {
                newPaper[row][column] = paper[row][column] || (numberOfColumns - column < paper[0].size && paper[row][numberOfColumns - column])
            }
        }

        return newPaper
    }

    private fun foldHorizontally(paper: TransparentPaper, yValue: Int) : TransparentPaper {
        val newPaper = Array(yValue ) { Array(paper[0].size) { false } }
        val numberOfRows = if (paper.size % 2 != 0) { paper.size - 1 } else { paper.size }

        for (row in newPaper.indices) {
            for (column in (newPaper[row].indices)) {
                newPaper[row][column] = paper[row][column] || (numberOfRows - row < paper.size && paper[numberOfRows - row][column])
            }
        }

        return newPaper
    }

}

typealias TransparentPaper = Array<Array<Boolean>>

fun TransparentPaper.printToFile(fileName: String) {
    File(fileName).writeText(this.joinToString("\n") { it.joinToString("") { value -> if (value) "#" else "." } })
}

fun TransparentPaper.print() {
    println(this.joinToString("\n") { it.joinToString("") { value -> if (value) "#" else "." } })
}

data class Input(
    val paper: TransparentPaper,
    val instructions: List<FoldInstruction>
)

data class FoldInstruction(
    val direction: Direction,
    val axis: Int
)

enum class Direction {
    VERTICAL,
    HORIZONTAL
}