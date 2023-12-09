package me.stepbystep

fun day9Part1() {
    readInputLines(9)
        .map { line ->
            line
                .split(" ")
                .map { it.toInt() }
        }
        .sumOf { it.computeNextValue() }
        .println()
}

fun day9Part2() {
    readInputLines(9)
        .map { line ->
            line
                .split(" ")
                .map { it.toInt() }
        }
        .sumOf { it.computePrevValue() }
        .println()
}

private fun List<Int>.computeNextValue(): Int {
    if (all { it == 0 }) return 0

    val diffs = List(size - 1) { index ->
        get(index + 1) - get(index)
    }
    val nextValue = last() + diffs.computeNextValue()
    return nextValue
}

private fun List<Int>.computePrevValue(): Int {
    if (all { it == 0 }) return 0

    val diffs = List(size - 1) { index ->
        get(index + 1) - get(index)
    }
    val prevValue = first() - diffs.computePrevValue()
    return prevValue
}