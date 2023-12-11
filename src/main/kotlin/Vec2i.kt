package me.stepbystep

data class Vec2i(
    val x: Int,
    val y: Int,
) : Comparable<Vec2i> {
    override fun compareTo(other: Vec2i): Int {
        if (x != other.x) return x - other.x
        if (y != other.y) return y - other.y

        return 0
    }
}