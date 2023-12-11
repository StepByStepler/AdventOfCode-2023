package me.stepbystep

/**
 * Note: you need to increase JVM stack size for running this (for example, -Xss258m work for me,
 *  probably less value will work too)
 */
fun day10Part1() {
    val field = readField()
    val start = field.map.asIterable()
        .first { it.value == Pipe.Start }
        .key

    val shortestPaths = mutableMapOf(start to 0)
    fillShortestPaths(field, shortestPaths, start)
    shortestPaths.values.max().println()
}

fun day10Part2() {
    val field = readField()
    val start = field.map.asIterable()
        .first { it.value == Pipe.Start }
        .key

    val shortestPaths = mutableMapOf(start to 0)
    fillShortestPaths(field, shortestPaths, start)

    field.map.keys
        .filter { it !in shortestPaths.keys }
        .count { position -> isInside(field, shortestPaths.keys, position) }
        .println()
}

private fun isInside(
    field: Field,
    loop: Set<Vector>,
    startPosition: Vector,
): Boolean {
    var timesCrossed = 0
    for (x in startPosition.first downTo 0) {
        val bottomPos = Vector(x, startPosition.second).takeIf { it in loop } ?: continue
        val toTopOffset = Vector(0, 1)
        val toBottomOffset = -toTopOffset
        val topPos = (bottomPos + toTopOffset).takeIf { it in loop } ?: continue
        val bottomPipe = field.map[bottomPos] ?: continue
        val topPipe = field.map[topPos] ?: continue
        if (bottomPipe.connections.none { it.offset == toTopOffset }) continue
        if (topPipe.connections.none { it.offset == toBottomOffset }) continue

        timesCrossed++
    }

    return timesCrossed % 2 == 1
}

private fun fillShortestPaths(
    field: Field,
    paths: MutableMap<Vector, Int>,
    currentPoint: Vector,
) {
    val currentPipe = field.map[currentPoint]!!
    val currentLength = paths.getValue(currentPoint)

    for (direction in currentPipe.connections) {
        val neighbourPoint = currentPoint + direction.offset
        val neighbour = field.map[neighbourPoint] ?: continue
        val inverseOffset = -direction.offset
        if (neighbour.connections.none { it.offset == inverseOffset }) continue

        val oldLength = paths[neighbourPoint]
        val newLength = currentLength + 1
        if (oldLength == null || oldLength > newLength) {
            paths[neighbourPoint] = newLength
            fillShortestPaths(field, paths, neighbourPoint)
        }
    }
}

private fun readField(): Field {
    return readInputLines(10)
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, symbol ->
                Triple(x, y, Pipe.bySymbol(symbol))
            }
        }
        .associate { (x, y, pipe) -> (x to y) to pipe }
        .let { Field(it) }
}

private operator fun Vector.plus(other: Vector) =
    Vector(first + other.first, second + other.second)

private operator fun Vector.unaryMinus() =
    Vector(-first, -second)

typealias Vector = Pair<Int, Int>

enum class Direction(
    val offset: Vector,
) {
    Left(-1 to 0),
    Right(1 to 0),
    Top(0 to -1),
    Bottom(0 to 1),
}

enum class Pipe(
    val symbol: Char,
    val connections: List<Direction>,
) {
    Start('S', Direction.entries),
    LeftTop('J', listOf(Direction.Left, Direction.Top)),
    LeftBottom('7', listOf(Direction.Left, Direction.Bottom)),
    RightTop('L', listOf(Direction.Right, Direction.Top)),
    RightBottom('F', listOf(Direction.Right, Direction.Bottom)),
    Vertical('|', listOf(Direction.Top, Direction.Bottom)),
    Horizontal('-', listOf(Direction.Left, Direction.Right)),
    ;

    companion object {
        private val bySymbol = entries.associateBy { it.symbol }

        fun bySymbol(symbol: Char) = bySymbol[symbol]
    }
}

private data class Field(
    val map: Map<Vector, Pipe?>,
)