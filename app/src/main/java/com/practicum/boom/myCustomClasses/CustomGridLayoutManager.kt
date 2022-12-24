package com.practicum.boom.myCustomClasses

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


open class CustomGridLayoutManager(
    context: Context?, int: Int,
    private val isScrollEnabled: Boolean = true
) : GridLayoutManager(context, int) {
   var enable=false
   private fun scrollEnable(): Int {
        return if (enable) 1
        else 0
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled //&& super.canScrollVertically()
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return super.scrollVerticallyBy(dy * scrollEnable(), recycler, state)
    }
}