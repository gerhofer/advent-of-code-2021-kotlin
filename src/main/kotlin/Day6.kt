object Day6 {

    fun part1(filePath: String) {
        val lanternFishes = javaClass.getResource(filePath).readText()
            .split(",")
            .map { it.trim().toInt() }
            .toMutableList()

        for (day in (1..80)) {
            val newFishes = lanternFishes.count { it == 0 }
            lanternFishes.replaceAll {
                if (it == 0) {
                    6
                } else {
                    it - 1
                }
            }
            repeat(newFishes) {
                lanternFishes.add(8)
            }
        }

        println(lanternFishes.size)
    }

    fun part2(filePath: String) {
        val lanternFishCount = javaClass.getResource(filePath).readText()
            .split(",")
            .map { it.trim().toInt() }
            .groupBy { it }
            .map { (key, value ) -> value.size * getLanternFishCount(key) }
            .sum()

        println(lanternFishCount)
    }

    fun getLanternFishCount(currentValue: Int): Long {
        var count = 1L
        for (i in (currentValue)..(DAYS-1) step 7) {
            if (8 + 1 + i > DAYS) {
                count += 1
            } else {
                count += getLanternFishCount(8 + 1 + i)
            }
        }
        return count
    }

}

const val DUPLICATION_TIME = 6
const val INITIAL_DUPLICATION_TIME = 8
const val DAYS = 256