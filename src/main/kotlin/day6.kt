package me.stepbystep

import kotlin.math.*

fun day6Part1() {
    solve("".toRegex())
}

fun day6Part2() {
    solve("\\s+".toRegex())
}

private fun solve(replacedSpace: Regex) {
    val (timeText, distText) = readInputLines(6)
    val races = timeText.readSeries(replacedSpace).zip(distText.readSeries(replacedSpace))
    races.fold(1L) { acc, (time, record) ->
        val root = sqrt(time * time - 4 * record.toDouble())
        val left = ceil(((time - root) / 2).nextUp()).toLong()
        val right = floor(((time + root) / 2).nextDown()).toLong()
        val result = right - left + 1
        acc * result
    }.println()
}

private fun String.readSeries(replacedSpace: Regex): List<Long> =
    substringAfter(':')
        .trim()
        .replace(replacedSpace, "")
        .split("\\s+".toRegex())
        .map { it.toLong() }