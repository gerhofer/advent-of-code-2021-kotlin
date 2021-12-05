object Day4 {

    fun part1(filePath: String) {
        val bingo = readBingo(filePath)

        for (number in bingo.drawnNumbers) {
            bingo.players.forEach { player ->
                player.mark(number)
                if (player.hasWon()) {
                    println(number * player.getScore())
                    return
                }
            }
        }

    }

    fun part2(filePath: String) {
        val bingo = readBingo(filePath)
        val boardsInGame = bingo.players.toMutableList()

        for (number in bingo.drawnNumbers) {
            boardsInGame.forEach { player ->
                player.mark(number)
                if (boardsInGame.none { !it.hasWon() } && player.hasWon()) {
                    println(player.getScore() * number)
                    return
                }
            }
            boardsInGame.removeIf { it.hasWon() }
        }
    }

    fun readBingo(filePath: String): BingoGame {
        val areas = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        val drawnNumbers = areas.first().split(",").map { it.toInt() }
        val players = areas.drop(1).map { field ->
            Bingo(field.split("\r\n").map { row ->
                row.trim().split("\\s+".toRegex())
                    .map { Cell(it.toInt(), false) }
            })
        }

        return BingoGame(drawnNumbers, players)
    }

}

data class BingoGame(
    val drawnNumbers: List<Int>,
    val players: List<Bingo>
)

data class Bingo(
    val field: List<List<Cell>>
) {

    fun mark(number: Int) {
        field.flatten()
            .filter { it.value == number }
            .forEach { it.marked = true }
    }

    fun hasWon(): Boolean {
        val rowFilled = field.any {
            it.all { cell -> cell.marked }
        }
        val columnFilled = (0..4).any { idx ->
            field.map { it[idx] }.all { cell -> cell.marked }
        }
        return rowFilled || columnFilled
    }

    fun getScore(): Int {
        return field.flatten()
            .filter { !it.marked }
            .sumOf { it.value }
    }

}

data class Cell(
    val value: Int,
    var marked: Boolean
)