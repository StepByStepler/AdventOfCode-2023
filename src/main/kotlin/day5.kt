package me.stepbystep

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun day5Part1() {
    val almanack = readAlmanackPart1()
    almanack
        .seeds
        .minOf { seed ->
            almanack
                .conversionRules
                .fold(seed) { index, conversion ->
                    conversion.firstNotNullOfOrNull { it.convertIfPossible(index) }
                        ?: index
                }
        }
        .println()
}

fun day5Part2() = runBlocking {
    val almanack = readAlmanackPart2()

    almanack
        .seeds
        .map { seedRange ->
            async(Dispatchers.IO) {
                seedRange.minOf { seed ->
                    almanack
                        .conversionRules
                        .fold(seed) { index, conversion ->
                            conversion.firstNotNullOfOrNull { it.convertIfPossible(index) }
                                ?: index
                        }
                }
            }
        }
        .minOf { it.await() }
        .println()
}

private fun readAlmanackPart1(): Almanack {
    val inputSections = readInputLines(5)
        .joinToString(separator = "\n")
        .split("\n\n")

    val seeds = inputSections
        .first()
        .substringAfter("seeds:")
        .trim()
        .split(" ")
        .map { it.toLong() }

    val conversions = inputSections
        .drop(1)
        .map { conversionText ->
            conversionText
                .split("\n")
                .drop(1)
                .map {
                    val args = it.split(" ")
                    // indexes are correct here
                    ConversionRule(
                        sourceStart = args[1].toLong(),
                        targetStart = args[0].toLong(),
                        length = args[2].toLong(),
                    )
                }
        }

    return Almanack(
        seeds = seeds,
        conversionRules = conversions,
    )
}

private fun readAlmanackPart2(): AlmanackV2 {
    val inputSections = readInputLines(5)
        .joinToString(separator = "\n")
        .split("\n\n")

    val seeds = inputSections
        .first()
        .substringAfter("seeds:")
        .trim()
        .split(" ")
        .chunked(2)
        .map { (sourceText, lengthText) ->
            val source = sourceText.toLong()
            val length = lengthText.toLong()
            source until (source + length)
        }

    val conversions = inputSections
        .drop(1)
        .map { conversionText ->
            conversionText
                .split("\n")
                .drop(1)
                .map {
                    val args = it.split(" ")
                    // indexes are correct here
                    ConversionRule(
                        sourceStart = args[1].toLong(),
                        targetStart = args[0].toLong(),
                        length = args[2].toLong(),
                    )
                }
        }

    return AlmanackV2(
        seeds = seeds,
        conversionRules = conversions,
    )
}

private data class Almanack(
    val seeds: List<Long>,
    val conversionRules: List<ConversionMap>,
)

private data class AlmanackV2(
    val seeds: List<LongRange>,
    val conversionRules: List<ConversionMap>,
)

private typealias ConversionMap = List<ConversionRule>

private data class ConversionRule(
    val sourceStart: Long,
    val targetStart: Long,
    val length: Long,
)

private fun ConversionRule.convertIfPossible(source: Long): Long? {
    val sourceRange = sourceStart until (sourceStart + length)
    return if (source in sourceRange) {
        source + (targetStart - sourceStart)
    } else {
        null
    }
}