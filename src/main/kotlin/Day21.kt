object Day21 {

    var player1Wins: Long = 0
    var player2Wins: Long = 0

    fun part1(filePath: String) {
        val (player1, player2) = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { it.substringAfter(":").trim().toInt() }

        var player1Position = player1
        var player2Position = player2
        var score1 = 0
        var score2 = 0
        var currentDice = 1
        var player1sTurn = true

        while (score1 < 1000 && score2 < 1000) {
            if (player1sTurn) {
                val newPosition = movePawn(player1Position, currentDice)
                player1Position = newPosition
                score1 += newPosition
                player1sTurn = false
            } else {
                val newPosition = movePawn(player2Position, currentDice)
                player2Position = newPosition
                score2 += newPosition
                player1sTurn = true
            }
            currentDice += 3
        }

        if (score1 > score2) {
            println((currentDice - 1) * score2)
        } else {
            println((currentDice - 1) * score1)
        }
    }

    fun movePawn(pawnPosition: Int, dice: Int): Int {
        val newPosition = pawnPosition + dice + dice + 1 + dice + 2
        val field = newPosition % 10
        return if (field == 0) {
            10
        } else {
            field
        }
    }

    fun moveSinglePawn(pawnPosition: Int, dice: Int): Int {
        val newPosition = pawnPosition + dice
        val field = newPosition % 10
        return if (field == 0) {
            10
        } else {
            field
        }
    }

    fun part2(filePath: String) {
        val (player1, player2) = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map { it.substringAfter(":").trim().toInt() }

        rollDice(Game(player1, 0, player2, 0,  true), 1)

        println(player1Wins)
        println(player2Wins)
    }

    fun rollDice(game: Game, worth: Int) {
        if (game.player1Score >= 21) {
            // println("player 1 wins $worth additional times")
            player1Wins += worth
            return
        }

        if (game.player2Score >= 21) {
            // println("player 2 wins $worth additional times")
            player2Wins += worth
            return
        }

        // move and add to score
        if (game.player1Turn) {
            val newPosition3 = moveSinglePawn(game.player1Position, 3)
            rollDice(game.copy(player1Position =  newPosition3, player1Score = game.player1Score + newPosition3, player1Turn = false), worth * 1)

            val newPosition4 = moveSinglePawn(game.player1Position, 4)
            rollDice(game.copy(player1Position =  newPosition4, player1Score = game.player1Score + newPosition4, player1Turn = false), worth * 3)

            val newPosition5 = moveSinglePawn(game.player1Position, 5)
            rollDice(game.copy(player1Position =  newPosition5, player1Score = game.player1Score + newPosition5, player1Turn = false), worth * 6)

            val newPosition6 = moveSinglePawn(game.player1Position, 6)
            rollDice(game.copy(player1Position =  newPosition6, player1Score = game.player1Score + newPosition6, player1Turn = false), worth * 7)

            val newPosition7 = moveSinglePawn(game.player1Position, 7)
            rollDice(game.copy(player1Position =  newPosition7, player1Score = game.player1Score + newPosition7, player1Turn = false), worth * 6)

            val newPosition8 = moveSinglePawn(game.player1Position, 8)
            rollDice(game.copy(player1Position =  newPosition8, player1Score = game.player1Score + newPosition8, player1Turn = false), worth * 3)

            val newPosition9 = moveSinglePawn(game.player1Position, 9)
            rollDice(game.copy(player1Position =  newPosition9, player1Score = game.player1Score + newPosition9, player1Turn = false), worth * 1)
        } else {
            val newPosition3 = moveSinglePawn(game.player2Position, 3)
            rollDice(game.copy(player2Position =  newPosition3, player2Score = game.player2Score + newPosition3, player1Turn = true), worth * 1)

            val newPosition4 = moveSinglePawn(game.player2Position, 4)
            rollDice(game.copy(player2Position =  newPosition4, player2Score = game.player2Score + newPosition4, player1Turn = true), worth * 3)

            val newPosition5 = moveSinglePawn(game.player2Position, 5)
            rollDice(game.copy(player2Position =  newPosition5, player2Score = game.player2Score + newPosition5, player1Turn = true), worth * 6)

            val newPosition6 = moveSinglePawn(game.player2Position, 6)
            rollDice(game.copy(player2Position =  newPosition6, player2Score = game.player2Score + newPosition6, player1Turn = true), worth * 7)

            val newPosition7 = moveSinglePawn(game.player2Position, 7)
            rollDice(game.copy(player2Position =  newPosition7, player2Score = game.player2Score + newPosition7, player1Turn = true), worth * 6)

            val newPosition8 = moveSinglePawn(game.player2Position, 8)
            rollDice(game.copy(player2Position =  newPosition8, player2Score = game.player2Score + newPosition8, player1Turn = true), worth * 3)

            val newPosition9 = moveSinglePawn(game.player2Position, 9)
            rollDice(game.copy(player2Position =  newPosition9, player2Score = game.player2Score + newPosition9, player1Turn = true), worth * 1)
        }
    }

    data class Game(
        var player1Position: Int,
        var player1Score: Int,
        var player2Position: Int,
        var player2Score: Int,
        var player1Turn: Boolean = true
    )
}