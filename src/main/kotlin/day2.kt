package me.stepbystep

fun day2Part1() {
    readInputLines(2)
        .parseGames()
        .filter { game ->
            game.series.all { period ->
                val red = period[Color.Red] ?: Int.MIN_VALUE
                val green = period[Color.Green] ?: Int.MIN_VALUE
                val blue = period[Color.Blue] ?: Int.MIN_VALUE
                red <= 12 && green <= 13 && blue <= 14
            }
        }
        .sumOf { it.id }
        .println()
}

fun day2Part2() {
    readInputLines(2)
        .parseGames()
        .sumOf { game ->
            Color.entries
                .map { color ->
                    game.series.maxOf { it[color] ?: 0 }
                }
                .fold(1) { acc, next -> acc * next }
                .toInt()
        }
        .println()
}

private fun List<String>.parseGames(): List<Game> = map { line ->
    val idRegex = Regex("Game (\\d+): (.+)")
    val idResult = idRegex.matchEntire(line) ?: error("Wrong string: $line")
    val (id, seriesText) = idResult.destructured
    val series = seriesText
        .split("; ")
        .map { period ->
            period.split(", ")
                .associate { pair ->
                    val (amount, color) = pair.split(" ")
                    Color.valueOf(color.capitalized()) to amount.toInt()
                }
        }

    Game(
        id = id.toInt(),
        series = series,
    )
}

private enum class Color {
    Green,
    Blue,
    Red,
}

private data class Game(
    val id: Int,
    val series: List<Map<Color, Int>>,
)