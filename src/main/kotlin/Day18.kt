import kotlin.math.ceil
import kotlin.math.floor

object Day18 {

    fun part1(filePath: String) {
        val reducedSum = javaClass.getResource(filePath).readText()
            .split("\r\n")
            .reduce { a, b ->
                val afterAddition = add(a, b)
                reduce(afterAddition)
            }

        println(getMagnitude(reducedSum))
    }

    private fun add(snailFishNumberA: String, snailFishNumberB: String): String {
        return "[$snailFishNumberA,$snailFishNumberB]"
    }

    private fun reduce(smallFishNumber: String): String {
        var reducedNumber = smallFishNumber
        var idx = 0
        var currentOpeningBrackets = 0
        var isDigit = false
        var checkingExplodes = true
        while (idx < reducedNumber.length) {
            if (checkingExplodes) {
                if (reducedNumber[idx] == '[') {
                    currentOpeningBrackets++
                } else if (reducedNumber[idx] == ']') {
                    currentOpeningBrackets--
                }

                if (currentOpeningBrackets == 5) {
                    reducedNumber = explode(reducedNumber, idx)
                    idx = -1
                    currentOpeningBrackets = 0
                }

                if (idx == reducedNumber.length - 1) {
                    checkingExplodes = false
                    idx = -1
                    isDigit = false
                }
            } else {
                isDigit = if (reducedNumber[idx].isDigit()) {
                    if (isDigit) {
                        reducedNumber = split(reducedNumber, idx)
                        checkingExplodes = true
                        idx = -1
                        currentOpeningBrackets = 0
                        false
                    } else {
                        true
                    }
                } else {
                    false
                }

                if (idx == reducedNumber.length - 1) {
                    return reducedNumber
                }
            }

            idx++
        }
        return reducedNumber
    }

    private fun explode(snailFishNumber: String, explodeAt: Int): String {
        val numbersToExplode = snailFishNumber.substring(explodeAt + 1).substringBefore("]")
        var beforeExplosion = snailFishNumber.substring(0, explodeAt)
        var afterExplosion = snailFishNumber.substring(explodeAt + 2 + numbersToExplode.length)
        val (moveToLeft, moveToRight) = numbersToExplode
            .split(",")
            .map { it.toInt() }

        for (i in (beforeExplosion.length - 1 downTo 0)) {
            if (beforeExplosion[i].isDigit()) {
                var number = beforeExplosion[i].toString()
                var digitIdx = i - 1
                while (beforeExplosion[digitIdx].isDigit()) {
                    number += beforeExplosion[digitIdx]
                    digitIdx--
                }
                val newNumber = number.reversed().toInt() + moveToLeft
                beforeExplosion = beforeExplosion.replaceRange(i - number.length + 1, i + 1, newNumber.toString())
                break
            }
        }

        for (i in afterExplosion.indices) {
            if (afterExplosion[i].isDigit()) {
                var number = afterExplosion[i].toString()
                var digitIdx = i + 1
                while (afterExplosion[digitIdx].isDigit()) {
                    number += afterExplosion[digitIdx]
                    digitIdx++
                }
                val newNumber = number.toInt() + moveToRight
                afterExplosion = afterExplosion.replaceRange(i, i + number.length, newNumber.toString())
                break
            }
        }

        //println("after explosion: ${beforeExplosion}0$afterExplosion")
        return beforeExplosion + "0" + afterExplosion
    }

    private fun split(snailFishNumber: String, splitAt: Int): String {
        val numberToSplitAsString = snailFishNumber.substring(splitAt - 1).substringBefore("]").substringBefore(",")
        val beforeSplit = snailFishNumber.substring(0, splitAt - 1)
        val afterSplit = snailFishNumber.substring(splitAt - 1 + numberToSplitAsString.length)
        val numberToSplit = numberToSplitAsString.toInt()
        val first = floor(numberToSplit / 2.0).toInt()
        val second = ceil(numberToSplit / 2.0).toInt()
        //println("after split    : $beforeSplit[$first,$second]$afterSplit")
        return "$beforeSplit[$first,$second]$afterSplit"
    }

    private fun getMagnitude(snailFishNumber: String, startIdx: Int = 0): Pair<Long, Int> {
        var first: Long = 0L
        var second: Long = 0L
        var letterIdx = startIdx
        while (letterIdx < snailFishNumber.length) {
            val letter = snailFishNumber[letterIdx]
            if (letter.isDigit()) {
                return Pair(letter.toString().toLong(), 1)
            } else if (letter == '[') {
                val (magnitudeOfLeft, idxIncrement) = getMagnitude(snailFishNumber, letterIdx + 1)
                first = magnitudeOfLeft
                letterIdx = (letterIdx + idxIncrement)
            } else if (letter == ',') {
                val (magnitudeOfRight, idxIncrement) = getMagnitude(snailFishNumber, letterIdx + 1)
                second = magnitudeOfRight
                letterIdx = (letterIdx + idxIncrement)
            } else if (letter == ']') {
                return Pair(3 * first + 2 * second, letterIdx - startIdx + 1)
            } else {
                error("should not happen - found character we should not find : $letter")
            }
            letterIdx++
        }
        error("should not happen")
    }


    fun part2(filePath: String) {
        val numbers = javaClass.getResource(filePath).readText()
            .split("\r\n")

        var maxMagnitude = 0L
        for (first in numbers.indices) {
            for (second in numbers.indices) {
                if (first != second) {
                    val (magnitude, _) = getMagnitude(reduce(add(numbers[first], numbers[second])))
                    if (magnitude > maxMagnitude) {
                        maxMagnitude = magnitude
                    }
                }
            }
        }

        println(maxMagnitude)
    }

}