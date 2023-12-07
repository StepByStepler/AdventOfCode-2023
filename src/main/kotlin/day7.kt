package me.stepbystep

fun day7Part1() {
    solve(withJoker = false)
}

fun day7Part2() {
    solve(withJoker = true)
}

private fun solve(withJoker: Boolean) {
    val weights = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        .withIndex()
        .associate { it.value to it.index }

    parseHands(withJoker)
        .sortedWith(compareByDescending<Hand> { it.combination }.thenComparing { h1, h2 ->
            for ((ch1, ch2) in h1.cards.zip(h2.cards)) {
                val diff = weights[ch2]!! - weights[ch1]!!
                if (diff != 0) return@thenComparing diff
            }

            0
        })
        .mapIndexed { index, hand ->
            hand.bet.toLong() * (index + 1)
        }
        .sum()
        .println()
}

private fun parseHands(withJoker: Boolean): List<Hand> =
    readInputLines(7).map { line ->
        val (cards, betText) = line.split(" ")
        Hand(
            cards = cards,
            bet = betText.toInt(),
            withJoker = withJoker,
        )
    }

enum class Combination {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    OnePair,
    HighCard,
}

private class Hand(
    val cards: String,
    val bet: Int,
    withJoker: Boolean,
) {
    val combination = cards.determineCombination(withJoker)
}

private fun String.determineCombination(withJoker: Boolean): Combination {
    val jokersCount = count { withJoker && it == 'J' }
    val counts = filter { !withJoker || it != 'J' }
        .groupingBy { it }
        .eachCount()
        .toList()
        .sortedByDescending { it.second }

    if (counts.isEmpty() || counts.size == 1) return Combination.FiveOfAKind
    if (counts.size == 2 && counts[0].second + jokersCount == 4) return Combination.FourOfAKind
    if (counts.size == 2 && counts[0].second + jokersCount == 3) return Combination.FullHouse
    if (counts.size == 3 && counts[0].second + jokersCount == 3) return Combination.ThreeOfAKind
    if (counts.size == 3 && counts[0].second + jokersCount == 2) return Combination.TwoPair
    if (counts.size == 4) return Combination.OnePair

    return Combination.HighCard
}
