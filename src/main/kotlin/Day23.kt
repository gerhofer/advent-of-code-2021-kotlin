import kotlin.math.abs

object Day23 {

    private val calculatedBoards = mutableSetOf<String>()

    val size = 4 // 2 für lvl 1
    val aEntrance = 2
    val bEntrance = 4
    val cEntrance = 6
    val dEntrance = 8
    val entrances = listOf(aEntrance, bEntrance, cEntrance, dEntrance)
    var minimumCost = Long.MAX_VALUE

    fun findOptimumPath() {

        val hallway = (0..10).map { HallwaySpot(it) }
        val firstColumn =  listOf('B', 'D', 'D', 'C')// lvl 1listOf('B', 'C')
        val secondColumn = listOf('C', 'B', 'C', 'B')// lvl 1 listOf('C', 'B')
        val thirdColumn =  listOf('A', 'A', 'B', 'D')// lvl 1listOf('A', 'D')
        val fourthColumn = listOf('A', 'C', 'A', 'D')// lvl 1 listOf('A', 'D')

        makeAllMoves(AmphipodState(hallway, firstColumn, secondColumn, thirdColumn, fourthColumn, 0), mutableListOf())

        println(minimumCost)
    }


    fun makeAllMoves(state: AmphipodState, path: MutableList<AmphipodState>) {
        path.add(state)
        if (state.cost > minimumCost || calculatedBoards.contains(state.toString())) {
            return
        }

        if (state.allDone()) {
            if (state.cost < minimumCost) {
                minimumCost = state.cost
            }
            for (step in path) {
                step.print()
            }
            println("found a solution with ${state.cost} cost")
            return
        }

        for (amphiodInHallway in state.hallway) {
            if (amphiodInHallway.occupant != null) {
                val goalArea = when (amphiodInHallway.occupant) {
                    'A' -> state.aColumn
                    'B' -> state.bColumn
                    'C' -> state.cColumn
                    'D' -> state.dColumn
                    else -> error("should not happen fournd ${amphiodInHallway.occupant}")
                }

                val goalEntrance = when (amphiodInHallway.occupant) {
                    'A' -> Entrance.A
                    'B' -> Entrance.B
                    'C' -> Entrance.C
                    'D' -> Entrance.D
                    else -> error("should not happen")
                }

                val range = if (amphiodInHallway.idx > goalEntrance.position) {
                    (goalEntrance.position until amphiodInHallway.idx)
                } else {
                    (amphiodInHallway.idx + 1..goalEntrance.position)
                }

                if (goalArea.size < size && goalArea.all { it == amphiodInHallway.occupant } &&
                    state.hallway.filter { it.idx in range }.none { it.isOccupied() }) {
                    val newBoard =
                        moveToEntrance(state, amphiodInHallway.idx, amphiodInHallway.occupant!!, goalEntrance)
                    makeAllMoves(newBoard, path)
                }
            }

        }

        if (state.dColumn.isNotEmpty() && state.dColumn.any { it != 'D' }) {
            val char = state.dColumn.last()
            val (hallways, entrances) = state.findAvailableSpots(dEntrance, char)
            if (entrances.isNotEmpty()) {
                val stateAfterMovement = moveToEntrance(state.copy(), Entrance.D, entrances.first())
                makeAllMoves(stateAfterMovement, path.toMutableList())
            } else {
                for (hallwaySpot in hallways) {
                    val stateAfterMovement = moveToHallwaySpot(state.copy(), Entrance.D, hallwaySpot)
                    makeAllMoves(stateAfterMovement,  path.toMutableList())
                }
            }
        }

        if (state.cColumn.isNotEmpty() && state.cColumn.any { it != 'C' }) {
            val char = state.cColumn.last()
            val (hallways, entrances) = state.findAvailableSpots(cEntrance, char)
            if (entrances.isNotEmpty()) {
                val stateAfterMovement = moveToEntrance(state.copy(), Entrance.C, entrances.first())
                makeAllMoves(stateAfterMovement,  path.toMutableList())
            } else {
                for (hallwaySpot in hallways) {
                    val stateAfterMovement = moveToHallwaySpot(state.copy(), Entrance.C, hallwaySpot)
                    makeAllMoves(stateAfterMovement,  path.toMutableList())
                }
            }
        }

        if (state.bColumn.isNotEmpty() && state.bColumn.any { it != 'B' }) {
            val char = state.bColumn.last()
            val (hallways, entrances) = state.findAvailableSpots(bEntrance, char)
            if (entrances.isNotEmpty()) {
                val stateAfterMovement = moveToEntrance(state.copy(), Entrance.B, entrances.first())
                makeAllMoves(stateAfterMovement,  path.toMutableList())
            } else {
                for (hallwaySpot in hallways) {
                    val stateAfterMovement = moveToHallwaySpot(state.copy(), Entrance.B, hallwaySpot)
                    makeAllMoves(stateAfterMovement,  path.toMutableList())
                }
            }
        }

        if (state.aColumn.isNotEmpty() && state.aColumn.any { it != 'A' }) {
            val char = state.aColumn.last()
            val (hallways, entrances) = state.findAvailableSpots(aEntrance, char)
            if (entrances.isNotEmpty()) {
                val stateAfterMovement = moveToEntrance(state.copy(), Entrance.A, entrances.first())
                makeAllMoves(stateAfterMovement,  path.toMutableList())
            } else {
                for (hallwaySpot in hallways) {
                    val stateAfterMovement = moveToHallwaySpot(state.copy(), Entrance.A, hallwaySpot)
                    makeAllMoves(stateAfterMovement,  path.toMutableList())
                }
            }
        }

        calculatedBoards.add(state.toString())
    }

    fun moveToHallwaySpot(state: AmphipodState, from: Entrance, goal: Int): AmphipodState {
        val aColumn = state.aColumn.toMutableList()
        val bColumn = state.bColumn.toMutableList()
        val cColumn = state.cColumn.toMutableList()
        val dColumn = state.dColumn.toMutableList()
        val fromColumn = when (from) {
            Entrance.A -> aColumn
            Entrance.B -> bColumn
            Entrance.C -> cColumn
            Entrance.D -> dColumn
        }

        val cost = abs(fromColumn.size - size) + 1 + abs(goal - from.position)
        val char = fromColumn.removeLast()
        val newHallway = state.hallway.toMutableList()
        newHallway[goal] = HallwaySpot(goal, char)

        return AmphipodState(
            newHallway.toList(),
            aColumn,
            bColumn,
            cColumn,
            dColumn,
            state.cost + cost * getCost(char)
        )
    }

    fun moveToEntrance(state: AmphipodState, from: Entrance, to: Entrance): AmphipodState {
        val aColumn = state.aColumn.toMutableList()
        val bColumn = state.bColumn.toMutableList()
        val cColumn = state.cColumn.toMutableList()
        val dColumn = state.dColumn.toMutableList()
        val fromColumn = when (from) {
            Entrance.A -> aColumn
            Entrance.B -> bColumn
            Entrance.C -> cColumn
            Entrance.D -> dColumn
        }

        val toColumn = when (to) {
            Entrance.A -> aColumn
            Entrance.B -> bColumn
            Entrance.C -> cColumn
            Entrance.D -> dColumn
        }

        val cost = abs(from.position - to.position) + 1 + abs(fromColumn.size - size) + abs(toColumn.size - size)
        val char = fromColumn.removeLast()
        toColumn.add(char)

        return AmphipodState(
            state.hallway.toList(),
            aColumn,
            bColumn,
            cColumn,
            dColumn,
            state.cost + cost * getCost(char)
        )
    }

    fun moveToEntrance(state: AmphipodState, from: Int, char: Char, to: Entrance): AmphipodState {
        val aColumn = state.aColumn.toMutableList()
        val bColumn = state.bColumn.toMutableList()
        val cColumn = state.cColumn.toMutableList()
        val dColumn = state.dColumn.toMutableList()

        val toColumn = when (to) {
            Entrance.A -> aColumn
            Entrance.B -> bColumn
            Entrance.C -> cColumn
            Entrance.D -> dColumn
        }

        val cost = abs(from - to.position) + abs(toColumn.size - size)
        toColumn.add(char)
        val hallway = state.hallway.toMutableList()
        hallway[from] = HallwaySpot(from, null)

        return AmphipodState(
            hallway.toList(),
            aColumn,
            bColumn,
            cColumn,
            dColumn,
            state.cost + cost * getCost(char)
        )
    }

    fun getCost(char: Char) = when (char) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> error("should not happen")
    }

    data class AmphipodState(
        val hallway: List<HallwaySpot>,
        val aColumn: List<Char>,
        val bColumn: List<Char>,
        val cColumn: List<Char>,
        val dColumn: List<Char>,
        val cost: Long
    ) {

        fun aDone() = aColumn.size == size && aColumn.all { it == 'A' }
        fun bDone() = bColumn.size == size && bColumn.all { it == 'B' }
        fun cDone() = cColumn.size == size && cColumn.all { it == 'C' }
        fun dDone() = dColumn.size == size && dColumn.all { it == 'D' }
        fun allDone() = aDone() && bDone() && cDone() && dDone()

        fun findAvailableSpots(startingPoint: Int, char: Char): PossibleMovements {
            val availableSpots = mutableListOf<Int>()
            val availableEntrances = mutableListOf<Entrance>()
            for (i in (startingPoint..10)) {
                if (hallway[i].occupant != null) {
                    break
                }
                // entrance spots must stay free
                if (i !in entrances) {
                    availableSpots.add(i)
                } else {
                    val entrance = Entrance.getEntrance(i)
                    val canEnterRoom = when (entrance) {
                        Entrance.A -> aColumn.size <= 1 && aColumn.all { it == char } && char == 'A'
                        Entrance.B -> bColumn.size <= 1 && bColumn.all { it == char } && char == 'B'
                        Entrance.C -> cColumn.size <= 1 && cColumn.all { it == char } && char == 'C'
                        Entrance.D -> dColumn.size <= 1 && dColumn.all { it == char } && char == 'D'
                    }
                    if (canEnterRoom) {
                        availableEntrances.add(entrance)
                    }
                }
            }

            for (i in (startingPoint downTo 0)) {
                if (hallway[i].occupant != null) {
                    break
                }
                // entrance spots must stay free
                if (i !in entrances) {
                    availableSpots.add(i)
                } else {
                    val entrance = Entrance.getEntrance(i)
                    val canEnterRoom = when (entrance) {
                        Entrance.A -> aColumn.size <= 1 && aColumn.all { it == char } && char == 'A'
                        Entrance.B -> bColumn.size <= 1 && bColumn.all { it == char } && char == 'B'
                        Entrance.C -> cColumn.size <= 1 && cColumn.all { it == char } && char == 'C'
                        Entrance.D -> dColumn.size <= 1 && dColumn.all { it == char } && char == 'D'
                    }
                    if (canEnterRoom) {
                        availableEntrances.add(entrance)
                    }
                }
            }

            return PossibleMovements(availableSpots, availableEntrances)
        }

        fun print() {
            if (size == 2) {
                val hallway = hallway.joinToString("") { hp -> hp.occupant?.toString() ?: "." }
                println()
                println("#".repeat(13))
                println("#$hallway#")
                println(
                    "###" + aColumn.getOrDefault(1) + "#" + bColumn.getOrDefault(1) + "#" + cColumn.getOrDefault(1) + "#" + dColumn.getOrDefault(
                        1
                    ) + "#"
                )
                println(
                    "  #" + aColumn.getOrDefault(0) + "#" + bColumn.getOrDefault(0) + "#" + cColumn.getOrDefault(0) + "#" + dColumn.getOrDefault(
                        0
                    ) + "#"
                )
                println("  #########")
                println("Cost now: $cost")
            } else {
                val hallway = hallway.joinToString("") { hp -> hp.occupant?.toString() ?: "." }
                println()
                println("#".repeat(13))
                println("#$hallway#")
                println(
                    "###" + aColumn.getOrDefault(3) + "#" + bColumn.getOrDefault(3) + "#" + cColumn.getOrDefault(3) + "#" + dColumn.getOrDefault(
                        3
                    ) + "#"
                )
                println(
                    "###" + aColumn.getOrDefault(2) + "#" + bColumn.getOrDefault(2) + "#" + cColumn.getOrDefault(2) + "#" + dColumn.getOrDefault(
                        2
                    ) + "#"
                )
                println(
                    "###" + aColumn.getOrDefault(1) + "#" + bColumn.getOrDefault(1) + "#" + cColumn.getOrDefault(1) + "#" + dColumn.getOrDefault(
                        1
                    ) + "#"
                )
                println(
                    "  #" + aColumn.getOrDefault(0) + "#" + bColumn.getOrDefault(0) + "#" + cColumn.getOrDefault(0) + "#" + dColumn.getOrDefault(
                        0
                    ) + "#"
                )
                println("  #########")
                println("Cost now: $cost")
            }
        }

        fun List<Char>.getOrDefault(idx: Int): Char {
            return if (idx !in this.indices) {
                '.'
            } else {
                this[idx]
            }
        }

    }

    data class PossibleMovements(
        val hallwayPosition: List<Int>,
        val entrances: List<Entrance>
    )

    enum class Entrance(val position: Int) {
        A(2), B(4), C(6), D(8);

        companion object {
            fun getEntrance(entrance: Int): Entrance = when (entrance) {
                2 -> A
                4 -> B
                6 -> C
                8 -> D
                else -> error("not an entrance")
            }
        }
    }

    data class HallwaySpot(
        val idx: Int,
        var occupant: Char? = null
    ) {
        fun isOccupied() = occupant != null
    }

}