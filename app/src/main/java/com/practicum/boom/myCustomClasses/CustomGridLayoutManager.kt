package com.practicum.boom.myCustomClasses

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


open class CustomGridLayoutManager(
    context: Context?, int: Int,
    private val isScrollEnabled: Boolean = true
) : GridLayoutManager(context, int) {
   var ScrollStatus=-1
   private fun scrollEnable(dy: Int): Int {
        return if (ScrollStatus==1&&dy>0||ScrollStatus==-1&&dy<0) 1
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
        //Log.i("test2", "ScrollStatus " + ScrollStatus)
       // Log.i("test2", "enab dy " + dy)
        return super.scrollVerticallyBy(dy * scrollEnable(dy), recycler, state)
    }
}