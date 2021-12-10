object Day10 {

    fun part1(filePath: String) {
        val corruptionScore = javaClass.getResource(filePath).readText()
            .split("\n")
            .sumOf { getCorruptionScore(it.trim()) }

        println(corruptionScore)
    }

    fun part2(filePath: String) {
        val autocompleteScores = javaClass.getResource(filePath).readText()
            .split("\n")
            .filter { getCorruptionScore(it.trim()) <= 0}
            .map { getAutocompleteScore(it.trim()) }
            .sorted()

        println(autocompleteScores[autocompleteScores.size / 2])
    }

    fun getCorruptionScore(line: String) : Int {
        val chunks = mutableListOf<Char>()

        for (character in line) {
            when(character) {
                in listOf('(', '[', '<', '{') -> chunks.add(character)
                in listOf(')', ']', '>', '}') -> {
                    if (chunks.last() != getOpposite(character)) {
                        return when(character) {
                            ')' -> 3
                            ']' -> 57
                            '}' -> 1197
                            '>' -> 25137
                            else -> 0
                        }
                    } else {
                        chunks.removeLast()
                    }
                }
            }

        }

        return 0
    }

    fun getAutocompleteScore(line: String) : Long {
        val chunks = mutableListOf<Char>()

        for (character in line) {
            when(character) {
                in listOf('(', '[', '<', '{') -> chunks.add(character)
                in listOf(')', ']', '>', '}') -> {
                    if (chunks.last() != getOpposite(character)) {
                        error("should not happen")
                    } else {
                        chunks.removeLast()
                    }
                }
            }
        }

        return getAutocompleteScore(chunks.reversed().map { getOpposite(it) })
    }

    fun getAutocompleteScore(characters: List<Char>) : Long {
        return characters
            .fold(0) { result, current ->
                result * 5 + getAutocompleteScore(current)
            }
    }

    fun getAutocompleteScore(character: Char) : Int = when(character) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> error("should not happen")
    }

    private fun getOpposite(character: Char) : Char = when(character) {
        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> error("should not happen")
    }

}