package com.practicum.boom

data class ScreenInfo(
    val widthInPixels: Int,
    val screenDensity: Float,
    private val heightProductIcon: Double
) {
    fun widthScreen(): Int {
        return (widthInPixels / screenDensity).toInt()
    }
//return height of product icon at recycler view
    fun heightOfProductIcon(): Int {
        return (widthInPixels * heightProductIcon/columnCount()).toInt()
    }
//return number of recycler view columns depending by screen resolution
    fun columnCount(): Int {
        return if (widthScreen() / 250 > 2) widthScreen() / 250 else 2 //если больше 2, то 250/2, иначе 2
    }
}
