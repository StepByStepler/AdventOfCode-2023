package me.stepbystep

import java.util.*

private object MyObject

fun readInput(day: Int): String {
    val fileName = "day$day.txt"
    return MyObject
        .javaClass
        .classLoader
        .getResourceAsStream(fileName)
        ?.bufferedReader()
        ?.readText()
        ?: error("Resource $fileName doesn't exist")
}

fun readInputLines(day: Int): List<String> =
    readInput(day).split("\n")

fun Any.println() = println(this)

fun String.capitalized(
    locale: Locale = Locale.getDefault(),
): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

fun <S, T> List<S>.cartesianProduct(other : List<T>) : List<Pair<S, T>> =
    this.flatMap { s ->
        List(other.size) { s }.zip(other)
    }
