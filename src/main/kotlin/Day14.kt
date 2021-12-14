object Day14 {

    fun part1(filePath: String) {
        val (pattern, mappings) = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        val polymeraseInclusions = parsePolymeraseInclusions(mappings)
        var patternInProgress = pattern

        for (i in 1..10) {
            val newPattern = patternInProgress.windowed(2).joinToString("") {
                val currentInclusion = polymeraseInclusions[it]
                "${it[0]}${currentInclusion}"
            }
            patternInProgress = newPattern + patternInProgress.last()
        }

        val lettersWithOccurance = patternInProgress.groupBy { it }
            .mapValues { it.value.size }
        val leastOccurances = lettersWithOccurance.minOf { it.value }
        val mostOccurances = lettersWithOccurance.maxOf { it.value }

        println("${mostOccurances} - $leastOccurances = ${mostOccurances - leastOccurances}")

    }

    private fun parsePolymeraseInclusions(mappings: String): Map<String, String> {
        val polymeraseInclusions = mappings.split("\r\n")
            .associate {
                val (first, second) = it.split(" -> ")
                first to second
            }
        return polymeraseInclusions
    }

    fun part2(filePath: String) {
        val (pattern, mappings) = javaClass.getResource(filePath).readText()
            .split("\r\n\r\n")

        val polymeraseInclusions = parsePolymeraseInclusions(mappings)
        var tupleOccurances = pattern.windowed(2)
            .groupBy { it }
            .mapValues { it.value.size.toLong() }

        for (i in 1..40) {
            var updatedTupleOccurances = mutableMapOf<String, Long>()

            for ((tuple, occurances) in tupleOccurances) {
                val characterToInsert = polymeraseInclusions[tuple] ?: "should not happen"

                val firstTuple = "${tuple[0]}$characterToInsert"
                updatedTupleOccurances[firstTuple] = updatedTupleOccurances.getOrDefault(firstTuple, 0) + occurances

                val secondTuple = "$characterToInsert${tuple[1]}"
                updatedTupleOccurances[secondTuple] = updatedTupleOccurances.getOrDefault(secondTuple, 0) + occurances
            }

            println(updatedTupleOccurances)

            tupleOccurances = updatedTupleOccurances
        }

        val lettersWithOccurance = tupleOccurances.keys.joinToString("")
            .associate { it to countOccurances(tupleOccurances, it, pattern.last()) }
        val leastOccurances = lettersWithOccurance.minOf { it.value }
        val mostOccurances = lettersWithOccurance.maxOf { it.value }

        println("${mostOccurances} - $leastOccurances = ${mostOccurances - leastOccurances}")
    }

    fun countOccurances(tuplesToOccurances: Map<String, Long>, lookup: Char, lastChar: Char) : Long {
        return tuplesToOccurances.filter { it.key.startsWith(lookup) }
            .values.sum() + (if (lookup == lastChar) { 1 } else { 0})
    }

}

