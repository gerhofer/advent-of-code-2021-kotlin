object Day3 {

    fun part1(filePath: String) {
        val binaryNumbers = javaClass.getResource(filePath).readText()
            .split("\r\n")

        val length = binaryNumbers.size

        var result = ""
        for (i in binaryNumbers[0].indices) {
            val zeroes = binaryNumbers.map { it[i] }
                .count { it == '0' }
            val ones = length - zeroes
            result += if (zeroes > ones) {
                "0"
            } else {
                "1"
            }
        }

        val invertedResult = getInvertedBits(result)

        println(result.toInt(2) * invertedResult.toInt(2))
    }

    private fun getInvertedBits(result: String) = result.map {
        if (it == '1') {
            '0'
        } else {
            '1'
        }
    }.joinToString("")

    fun part2(filePath: String) {
        val binaryNumbers = javaClass.getResource(filePath).readText()
            .split("\r\n")

        val oxygenCandidates = binaryNumbers.toMutableList()
        var i = 0
        while (oxygenCandidates.size != 1) {
            val zeroes = oxygenCandidates.map { it[i] }
                .count { it == '0' }
            val ones = oxygenCandidates.size - zeroes
            if (zeroes > ones) {
                oxygenCandidates.removeIf { it[i] == '1' }
            } else {
                oxygenCandidates.removeIf { it[i] == '0' }
            }
            i++
        }

        val co2ScrubberCandidates = binaryNumbers.toMutableList()
        var j = 0
        while (co2ScrubberCandidates.size != 1) {
            val zeroes = co2ScrubberCandidates.map { it[j] }
                .count { it == '0' }
            val ones = co2ScrubberCandidates.size - zeroes
            if (zeroes > ones) {
                co2ScrubberCandidates.removeIf { it[j] == '0' }
            } else {
                co2ScrubberCandidates.removeIf { it[j] == '1' }
            }
            j++
        }

        println(oxygenCandidates[0].toInt(2) * co2ScrubberCandidates[0].toInt(2))
    }

}