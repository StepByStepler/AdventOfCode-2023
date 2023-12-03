package me.stepbystep

fun day1Part1() {
    readInputLines(1).sumOf { line ->
        val first = line.first { it.isDigit() }.digitToInt()
        val last = line.last { it.isDigit() }.digitToInt()
        "$first$last".toInt()
    }.println()
}

fun day1Part2() {
    readInputLines(1)
        .sumOf { line ->
            val first = line.findNearest(false)
            val last = line.findNearest(true)
            "$first$last".toInt()
        }
        .println()
}

private val mapping = listOf(
    "zero" to 0,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
) + (0..9).map { "$it" to it }

private fun String.findNearest(reverseOrder: Boolean): Int {
    return mapping
        .map { (name, value) ->
            val startIndex = if (reverseOrder) lastIndexOf(name) else indexOf(name)
            if (startIndex < 0) return@map null

            val lengthDiff = if (reverseOrder) name.length else 0
            val signDiff = if (reverseOrder) -1 else 1
            val index = (startIndex + lengthDiff) * signDiff
            index to value
        }
        .filterNotNull()
        .minBy { it.first }
        .second
}