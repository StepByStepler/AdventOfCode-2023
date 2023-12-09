package me.stepbystep

fun day8Part1() {
    readNavigationMap()
        .stepsToReach(
            startingPoint = "AAA",
            isEndPoint = { it == "ZZZ" },
        )
        .println()
}

fun day8Part2() {
    val navigationMap = readNavigationMap()

    navigationMap.map.keys
        .filter { it.endsWith('A') }
        .map { startingPoint ->
            navigationMap
                .stepsToReach(
                    startingPoint = startingPoint,
                    isEndPoint = { it.endsWith('Z') },
                )
                .toLong()
        }
        .fold(1L) { lcm, steps -> lcm(lcm, steps) }
        .println()
}

private fun NavigationMap.stepsToReach(
    startingPoint: String,
    isEndPoint: (String) -> Boolean,
): Int {
    var steps = 0
    var currentPoint = startingPoint
    while (!isEndPoint(currentPoint)) {
        val direction = sequence[steps % sequence.length]
        val (left, right) = map.getValue(currentPoint)
        currentPoint = if (direction == 'L') left else right
        steps++
    }

    return steps
}

private fun readNavigationMap(): NavigationMap {
    val input = readInputLines(8)
    val sequence = input.first()
    val regex = "(\\w{3}) = \\((\\w{3}), (\\w{3})\\)".toRegex()
    val map = input
        .drop(2)
        .associate { line ->
            val (start, left, right) = regex.matchEntire(line)!!.destructured
            start to (left to right)
        }

    return NavigationMap(
        sequence = sequence,
        map = map,
    )
}

private fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

private data class NavigationMap(
    val sequence: String,
    val map: Map<String, Pair<String, String>>,
)