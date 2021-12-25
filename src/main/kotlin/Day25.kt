object Day25 {

    fun part1(filePath: String) {
        var field = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .map {
                it.split("").filter { letter -> letter.isNotEmpty() }.toMutableList()
            }.toMutableList()

        var anyMoved = true
        var counter = 0
        while (anyMoved) {
            anyMoved = false
            var newField = mutableListOf<MutableList<String>>()
            // moveRight
            for (i in field.indices) {
                val newRow = field[i].toMutableList()
                for (j in field[i].indices) {
                    val left = before(j, field[0].size)
                    if (field[i][left] == ">" && field[i][j] == ".") {
                        anyMoved = true
                        newRow[left] = "."
                        newRow[j] = ">"
                    }
                }
                newField.add(newRow)
            }

            field = newField
            newField = field.map {
                    list -> list.toMutableList()
            }.toMutableList()
            // moveDown
            for (j in field[0].indices) {
                for (i in field.indices) {
                    val top = before(i, field.size)
                    if (field[top][j] == "v" && field[i][j] == ".") {
                        anyMoved = true
                        newField[top][j] = "."
                        newField[i][j] = "v"
                    }
                }
            }
            counter ++
            field = newField
        }

        print(counter)

    }

    fun before(x: Int, size: Int): Int {
        return if (x > 0) {
            x - 1
        } else {
            size - 1
        }
    }

}