package com.practicum.boom

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager


open class CustomGridLayoutManager(
    context: Context?,
    int: Int,
    private val isScrollEnabled: Boolean = true
) : GridLayoutManager(context, int) {

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled //&& super.canScrollVertically()
    }
}