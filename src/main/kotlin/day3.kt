package me.stepbystep

import kotlin.math.abs

fun day3Part1() {
    val symbols = readInputLines(3).obtainSymbols()

    val numberRegex = Regex("\\d+")
    readInputLines(3)
        .mapIndexed { digitY, line ->
            numberRegex.findAll(line)
                .filter { result ->
                    symbols.any { (symX, symY, _) ->
                        isAdjacent(result.range, digitY, symX, symY)
                    }
                }
                .map { it.value.toInt() }
        }
        .flatMap { it.toList() }
        .sum()
        .println()
}

fun day3Part2() {
    val symbols = readInputLines(3).obtainSymbols()
    val numbers = readInputLines(3).obtainNumbers()

    symbols
        .filter { (_, _, ch) -> ch == '*' }
        .sumOf { (symX, symY, _) ->
            val adjacentNums = numbers
                .filter { (numRange, numY, _) ->
                    isAdjacent(numRange, numY, symX, symY)
                }
                .map { (_, _, num) -> num }

            when {
                adjacentNums.size != 2 -> 0
                else -> adjacentNums.first() * adjacentNums.last()
            }
        }
        .println()
}

private fun List<String>.obtainSymbols() =
    this
        .map {
            it.withIndex()
                .filter { (_, ch) -> !ch.isDigit() && ch != '.' }
        }
        .mapIndexed { index, list -> list.map { pair -> Triple(pair.index, index, pair.value) } }
        .flatten()

private val numberRegex = Regex("\\d+")

private fun List<String>.obtainNumbers() =
    this
        .mapIndexed { digitY, line ->
            numberRegex.findAll(line)
                .map { Triple(it.range, digitY, it.value.toInt()) }
        }
        .flatMap { it.toList() }

private fun isAdjacent(numRange: IntRange, numY: Int, symX: Int, symY: Int): Boolean =
    numRange.any { digitX ->
        abs(digitX - symX) <= 1 && abs(numY - symY) <= 1
    }