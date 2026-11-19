package com.Kebcaqa1.Veghar.domain.model

enum class GateFacing {
    LEFT,
    RIGHT;

    fun flipped(): GateFacing = if (this == LEFT) RIGHT else LEFT

    fun step(): Int = if (this == LEFT) -1 else 1
}

data class Gate(
    val id: Int,
    val row: Int,
    val lane: Int,
    val facing: GateFacing
)
