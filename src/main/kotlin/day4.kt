package me.stepbystep

import kotlin.math.pow

fun day4Part1() {
    parseMyCards()
        .sumOf { card ->
            when (val sameNumbers = card.winningNumbers.intersect(card.myNumbers).size) {
                0 -> 0
                else -> 2.0.pow(sameNumbers - 1).toInt()
            }
        }
        .println()
}

fun day4Part2() {
    val amounts = hashMapOf<Int, Int>()
    fun addAmount(id: Int, amount: Int) {
        val old = amounts[id] ?: 0
        amounts[id] = old + amount
    }

    parseMyCards().forEach { card ->
        addAmount(card.id, 1)
        val sameNumbers = card.winningNumbers.intersect(card.myNumbers).size
        val thisCardAmount = amounts[card.id] ?: 0
        for (i in (card.id + 1)..(card.id + sameNumbers)) {
            addAmount(i, thisCardAmount)
        }
    }

    amounts.values.sum().println()
}

private val cardRegex = Regex("Card\\s+(\\d+):\\s+(.+)\\s+\\|\\s+(.+)")
private val spaceRegex = Regex("\\s+")
private fun parseMyCards(): List<Card> =
    readInputLines(4).map { line ->
        val (idText, winningText, myText) = cardRegex.matchEntire(line)!!.destructured
        Card(
            id = idText.toInt(),
            winningNumbers = winningText.split(spaceRegex).map { it.toInt() }.toSet(),
            myNumbers = myText.split(spaceRegex).map { it.toInt() }.toSet(),
        )
    }

private data class Card(
    val id: Int,
    val winningNumbers: Set<Int>,
    val myNumbers: Set<Int>,
)