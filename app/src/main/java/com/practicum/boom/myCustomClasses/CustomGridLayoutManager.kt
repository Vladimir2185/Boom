package com.practicum.boom.myCustomClasses

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.home.MainHomeFragment.Companion.SCROLL_STATUS_DOWN
import com.practicum.boom.home.MainHomeFragment.Companion.SCROLL_STATUS_UP


open class CustomGridLayoutManager(
    context: Context?, int: Int,
    private val isScrollEnabled: Boolean = true
) : GridLayoutManager(context, int) {
   var scrollStatus=SCROLL_STATUS_DOWN
   private fun scrollEnable(dy: Int): Int {
        return if (scrollStatus==SCROLL_STATUS_UP&&dy>0||scrollStatus==SCROLL_STATUS_DOWN&&dy<0) 1
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