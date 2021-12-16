object Day16 {

    fun part1(filePath: String) {
        val instructions = javaClass.getResource(filePath).readText()
            .split("\r\n")

        val instructionsInBinary = convertToBinaryString(instructions)

        for (instruction in instructionsInBinary) {
            val (parsedInstruction, offset) = parseInstruction(instruction.joinToString(""), 0)
            println(sumVersions(parsedInstruction))
        }

    }

    fun part2(filePath: String) {
        val instructions = javaClass.getResource(filePath).readText()
            .split("\r\n")

        val instructionsInBinary = convertToBinaryString(instructions)

        for (instruction in instructionsInBinary) {
            val (parsedInstruction, offset) = parseInstruction(instruction.joinToString(""), 0)
            println(evaluate(parsedInstruction))
        }
    }

    private fun evaluate(instruction: Instruction) : Long {
        if (instruction.literalValue != null) {
            return instruction.literalValue
        }
        return when(instruction.typeId) {
            0 -> instruction.subPackets.sumOf { evaluate(it) }
            1 -> instruction.subPackets.fold(1) { product, current -> product * evaluate(current) }
            2 -> instruction.subPackets.minOf { evaluate(it) }
            3 -> instruction.subPackets.maxOf { evaluate(it) }
            5 -> {
                val (first, second) = instruction.subPackets.map { evaluate(it) }
                if (first > second) {
                    1L
                } else {
                    0L
                }
            }
            6 -> {
                val (first, second) = instruction.subPackets.map { evaluate(it) }
                if (first < second) {
                    1L
                } else {
                    0L
                }
            }
            7 -> {
                val (first, second) = instruction.subPackets.map { evaluate(it) }
                if (first == second) {
                    1L
                } else {
                    0L
                }
            }
            else -> error("invalid type id")
        }
    }

    private fun convertToBinaryString(instructions: List<String>) =
        instructions.map { instruction ->
            instruction.map { hexDigit ->
                hexDigit.toString().toInt(16).toString(2).padStart(4, '0')
            }
        }

    private fun sumVersions (instruction: Instruction) : Int {
        var sum = instruction.version
        for (subInstruction in instruction.subPackets) {
            sum += sumVersions(subInstruction)
        }
        return sum
    }

    private fun parseInstruction(instruction: String, initialIndex: Int): InstructionAndOffset {
        var parseIndex = initialIndex
        val version = instruction.substring(parseIndex, parseIndex + VERSION_SIZE).toInt(2)
        val typeId = instruction.substring(parseIndex + VERSION_SIZE, parseIndex + VERSION_SIZE + TYPE_SIZE).toInt(2)
        parseIndex += VERSION_SIZE + TYPE_SIZE
        return when (typeId) {
            LITERAL_TYPE -> {
                val (literal, idxIncrement) = parseLiteral(instruction, parseIndex)
                parseIndex += idxIncrement
                InstructionAndOffset(Instruction(version, typeId, literal), parseIndex - initialIndex)
            }
            else -> {
                val (subpackets, idxIncrement) = parseOperator(instruction, parseIndex)
                parseIndex += idxIncrement
                InstructionAndOffset(Instruction(version, typeId, null, subpackets), parseIndex - initialIndex)
            }
        }
    }

    fun parseOperator(instruction: String, idx: Int) : PacketsAndOffset {
        var idxOffset = 0
        val subPackets = mutableListOf<Instruction>()
        val lengthType = instruction.substring(idx, idx + 1).toInt(2)
        if (lengthType == 0) {
            val packetLength = instruction.substring(idx + 1, idx + 1 + 15).toInt(2)
            idxOffset += 16
            val goal = idxOffset + packetLength
            while (idxOffset < goal) {
                val (instruction, idxIncrement) = parseInstruction(instruction, idx + idxOffset)
                subPackets.add(instruction)
                idxOffset += idxIncrement
            }
        } else {
            val numberOfPackets = instruction.substring(idx + 1, idx + 1 + 11).toInt(2)
            idxOffset += 12
            while (subPackets.size < numberOfPackets) {
                val (instruction, idxIncrement) = parseInstruction(instruction, idx + idxOffset)
                subPackets.add(instruction)
                idxOffset += idxIncrement
            }
        }
        return PacketsAndOffset(subPackets, idxOffset)
    }

    fun parseLiteral(instruction: String, idx: Int) : Pair<Long, Int> {
        var digit = ""
        var idxIncrements = 0
        for (batchOfFive in instruction.substring(idx).chunked(5)) {
            idxIncrements += 5
            digit += batchOfFive.drop(1)
            if (batchOfFive.startsWith("0")) {
                return Pair(digit.toLong(2), idxIncrements)
            }
        }
        error("Should not happen")
    }

    data class Instruction(
        val version: Int,
        val typeId: Int,
        val literalValue: Long? = null,
        val subPackets: MutableList<Instruction> = mutableListOf()
    )
    
    data class InstructionAndOffset(
        val instruction: Instruction,
        val idxOffset: Int
    )

    data class PacketsAndOffset(
        val packets: MutableList<Instruction> = mutableListOf(),
        val idxOffset: Int
    )

    const val LITERAL_TYPE = 4
    const val VERSION_SIZE = 3
    const val TYPE_SIZE = 3

}