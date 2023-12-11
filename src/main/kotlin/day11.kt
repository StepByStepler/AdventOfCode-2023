package me.stepbystep

fun day11Part1() {
    readUniverse().expanded().solve(gapSize = 2)
}

fun day11Part2() {
    readUniverse().expanded().solve(gapSize = 1000000)
}

private fun Universe.solve(gapSize: Int) {
    val allGalaxies = flatMapIndexed { y, list ->
        list
            .mapIndexed { x, ch -> Vec2i(x, y) to ch }
            .filter { (_, ch) -> ch == Galaxy }
            .map { it.first }
    }

    allGalaxies.cartesianProduct(allGalaxies)
        .filter { (first, second) -> first != second }
        .distinctBy { (first, second) ->
            listOf(first, second).sorted()
        }
        .sumOf { (first, second) ->
            var totalSteps = 0L

            val xDirection = if (second.x > first.x) 1 else -1
            var x = first.x
            while (x != second.x) {
                val currentType = this[first.y][x]
                totalSteps += if (currentType == Vertical || currentType == Horizontal) gapSize else 1
                x += xDirection
            }

            val yDirection = if (second.y > first.y) 1 else -1
            var y = first.y
            while (y != second.y) {
                val currentType = this[y][second.x]
                totalSteps += if (currentType == Horizontal || currentType == Vertical) gapSize else 1
                y += yDirection
            }

            totalSteps
        }
        .println()
}

private fun readUniverse(): Universe =
    readInputLines(11)
        .map { it.toCharArray().toList() }

private fun Universe.expanded(): Universe {
    val initial = this
    val withRows = buildList {
        for (row in initial) {
            if (row.all { it == Space }) {
                add(List(row.size) { Vertical })
            } else {
                add(row)
            }
        }
    }

    val withColumns = withRows.map { it.toMutableList() }
    for (index in withRows.first().lastIndex downTo 0) {
        if (withColumns.all { it[index] == Space || it[index] == Vertical }) {
            withColumns.forEach {
                it[index] = when {
                    it[index] == Vertical -> Both
                    else -> Horizontal
                }
            }
        }
    }

    return withColumns
}

private typealias Universe = List<List<Char>>
private const val Space = '.'
private const val Galaxy = '#'
private const val Vertical = 'V'
private const val Horizontal = 'H'
private const val Both = 'B'