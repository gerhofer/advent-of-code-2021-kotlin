import kotlin.math.floor

object Day24 {

    fun part1(filePath: String) {
        val instructions = parseInstructions(filePath)

        for (a in (9 downTo 1)) {
            for (c in (5 downTo 1)) {
                for (d in (9 downTo 1)) {
                    for (e in (9 downTo 1)) {
                        for (f in (3 downTo 1)) {
                            for (g in (9 downTo 1)) {
                                for (h in (9 downTo 1)) {
                                    for (i in (9 downTo 5)) {
                                        val digits =
                                            listOf(a, 1, c, d, d, c+4, 9, e, f, f + 6, g, h, i, i-4)
                                        val storageAfterProgramRan = checkMonad(instructions, digits)
                                        if (storageAfterProgramRan.isValid()) {
                                            println("found a valid monad ${digits.joinToString("")}")
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    fun part2(filePath: String) {
        val instructions = parseInstructions(filePath)

        for (a in (1..9)) {
            for (c in (1..5)) {
                for (d in (1..9)) {
                    for (e in (1..9)) {
                        for (f in (1..3)) {
                            for (g in (1..9)) {
                                for (h in (1..9)) {
                                    for (i in (5..9)) {
                                        val digits =
                                            listOf(a, 1, c, d, d, c+4, 9, e, f, f + 6, g, h, i, i-4)
                                        val storageAfterProgramRan = checkMonad(instructions, digits)
                                        if (storageAfterProgramRan.isValid()) {
                                            println("found a valid monad ${digits.joinToString("")}")
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    fun checkMonad(instructions: List<Instruction>, input: List<Int>): Storage {
        val storage = Storage(0, 0, 0, 0)
        var inputIdx = 0
        for (instruction in instructions) {
            when (instruction.operation) {
                "inp" -> {
                    storage.set(instruction.parameters[0], input[inputIdx].toLong())
                    inputIdx++
                }
                "add" -> {
                    val first = instruction.parameters[0]
                    val second = instruction.parameters[1]
                    storage.set(first, storage.getVariableOrValue(first) + storage.getVariableOrValue(second))
                }
                "mul" -> {
                    val first = instruction.parameters[0]
                    val second = instruction.parameters[1]
                    storage.set(first, storage.getVariableOrValue(first) * storage.getVariableOrValue(second))
                }
                "eql" -> {
                    val first = instruction.parameters[0]
                    val second = instruction.parameters[1]
                    storage.set(
                        first,
                        if (storage.getVariableOrValue(first) == storage.getVariableOrValue(second)) 1 else 0
                    )
                }
                "mod" -> {
                    val first = instruction.parameters[0]
                    val second = instruction.parameters[1]
                    storage.set(first, storage.getVariableOrValue(first) % storage.getVariableOrValue(second))
                }
                "div" -> {
                    val first = instruction.parameters[0]
                    val second = instruction.parameters[1]
                    storage.set(
                        first,
                        floor(
                            storage.getVariableOrValue(first) / storage.getVariableOrValue(second).toDouble()
                        ).toLong()
                    )
                }
            }
        }
        return storage
    }

    fun parseInstructions(filePath: String): List<Instruction> {
        return javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map {
                val input = it.split(" ")
                Instruction(input.first(), input.drop(1))
            }
    }

    data class Instruction(
        val operation: String,
        val parameters: List<String>
    )

    data class Storage(
        var w: Long,
        var x: Long,
        var y: Long,
        var z: Long
    ) {

        fun isValid() = z == 0L

        fun set(variable: String, value: Long) {
            when (variable) {
                "w" -> this.w = value
                "x" -> this.x = value
                "y" -> this.y = value
                "z" -> this.z = value
                else -> error("Invalid variable name")
            }
        }

        fun getVariableOrValue(variableOrValue: String): Long {
            return when (variableOrValue) {
                "w" -> this.w
                "x" -> this.x
                "y" -> this.y
                "z" -> this.z
                else -> variableOrValue.toLong()
            }
        }

    }

}