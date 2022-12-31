package com.practicum.boom.myCustomClasses

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

open class CustomScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var ScrollStatus = -1
    private var oldEvent = -1
    var onDispatchTouchEvent: OnDispatchTouchEvent? = null


    interface OnDispatchTouchEvent {
        fun onDispatchTouch(action_up: Boolean): Int
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        ev?.let {
            onDispatchTouchEvent?.let {
                if (oldEvent != MotionEvent.ACTION_DOWN && ev.action == MotionEvent.ACTION_UP && ScrollStatus==0) {
                    ScrollStatus = it.onDispatchTouch(true)
                } else ScrollStatus = it.onDispatchTouch(false)
            }
            oldEvent = it.action
        }

        return super.dispatchTouchEvent(ev)
    }

}
