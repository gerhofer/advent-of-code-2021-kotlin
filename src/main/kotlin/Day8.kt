object Day8 {

    fun part1(filePath: String) {
        val signalEntries = javaClass.getResource(filePath).readText()
            .split("\n")
            .map { SignalEntry(it) }

        val occurancesOfEasilyIdentifiableNumbers =
            signalEntries.map { entry -> entry.outputValue.count { it.length in listOf(2, 3, 4, 7) } }

        println(occurancesOfEasilyIdentifiableNumbers.sum())
    }

    fun part2(filePath: String) {
        val sumOfValues = javaClass.getResource(filePath).readText()
            .split("\n")
            .map { SignalEntry(it) }
            .sumOf {
                decodeOutput(it.outputValue, getPattern(it.signalPattern))
            }

        println(sumOfValues)
    }

    private fun getPattern(signalPattern: SignalPattern): Map<String, String> {
        val onePattern = signalPattern.findCandidatesByLength(2)
        val sevenPattern = signalPattern.findCandidatesByLength(3)
        val fourPattern = signalPattern.findCandidatesByLength(4)
        val mapping: Map<Int, MutableSet<String>> = mapOf(
            1 to (fourPattern - onePattern).toMutableSet(),
            2 to (sevenPattern - onePattern).toMutableSet(),
            3 to (onePattern).toMutableSet(),
            4 to (onePattern).toMutableSet(),
            5 to (allPatterns - (fourPattern + sevenPattern)).toMutableSet(),
            6 to (allPatterns - (fourPattern + sevenPattern)).toMutableSet(),
            7 to (fourPattern - onePattern).toMutableSet()
        )

        // 0 6 9
        val candidatesForSixDigitDisplay = signalPattern.filter { it.length == 6 }.toSet()
        for (candidate in candidatesForSixDigitDisplay) {
            val sixDigits = candidate.split("").filter { it.isNotBlank() }.toSet()

            if (containsMiddleSegment(sixDigits, mapping)) {
                if (containsBottomLeftSegment(sixDigits, mapping)) {
                    // must be a 9
                    mapping[6]!!.removeIf { it in sixDigits }
                    mapping[5]!!.removeIf { it in mapping[6]!! }
                } else {
                    // must be a 6
                    mapping[3]!!.removeIf { it in sixDigits }
                    mapping[4]!!.removeIf { it in mapping[3]!! }
                }
            } else {
                // must be a 0
                mapping[7]!!.removeIf { it in sixDigits }
                mapping[1]!!.removeIf { it in mapping[7]!! }
            }
        }

        return mapping.map { it.value.first() to digitToOriginalLetter[it.key]!! }.toMap()
    }

    private fun containsBottomLeftSegment(
        sixDigits: Set<String>,
        mapping: Map<Int, MutableSet<String>>
    ) = sixDigits.containsAll(mapping[3]!!) && sixDigits.containsAll(mapping[4]!!)

    private fun containsMiddleSegment(
        sixDigits: Set<String>,
        mapping: Map<Int, MutableSet<String>>
    ) = sixDigits.containsAll(mapping[1]!!) && sixDigits.containsAll(mapping[7]!!)

    private fun decodeOutput(outputValue: OutputValue, mapping: Map<String, String>): Long {
        return outputValue
            .map { singleDigit ->
                singleDigit.map { mapping[it.toString()]!! }
                    .sorted().joinToString("")
            }.joinToString("") { translatedDigit ->
                sevenSegmentDisplays[translatedDigit]!!.toString()
            }
            .toLong()
    }

}

val allPatterns = setOf("a", "b", "c", "d", "e", "f", "g")
val digitToOriginalLetter = mapOf(
    2 to "a", 1 to "b", 3 to "c", 7 to "d", 6 to "e", 4 to "f", 5 to "g"
)
val sevenSegmentDisplays = mapOf(
    "abcefg" to 0,
    "cf" to 1,
    "acdeg" to 2,
    "acdfg" to 3,
    "bcdf" to 4,
    "abdfg" to 5,
    "abdefg" to 6,
    "acf" to 7,
    "abcdefg" to 8,
    "abcdfg" to 9
)

typealias SignalPattern = List<String>

fun SignalPattern.findCandidatesByLength(size: Int): Set<String> {
    return this.first { it.length == size }.split("").filter { it.isNotBlank() }.toSet()
}

typealias OutputValue = List<String>

data class SignalEntry(
    val signalPattern: SignalPattern,
    val outputValue: OutputValue
) {
    constructor(string: String) : this(
        string.substringBefore("|")
            .trim()
            .split(" ")
            .filter { it.isNotBlank() },
        string.substringAfter("|")
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
    )
}