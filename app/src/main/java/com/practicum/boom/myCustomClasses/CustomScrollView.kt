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

    private var lock = false
    private var oldEvent = -1
    var onDispatchTouchEvent: OnDispatchTouchEvent? = null


    interface OnDispatchTouchEvent {
        fun onDispatchTouch(action_up: Boolean): Boolean
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        ev?.let {
            onDispatchTouchEvent?.let {
                if (oldEvent != MotionEvent.ACTION_DOWN && ev.action == MotionEvent.ACTION_UP && !lock) {
                    lock = it.onDispatchTouch(true)
                } else lock = it.onDispatchTouch(false)
            }
            oldEvent = it.action
        }

        return super.dispatchTouchEvent(ev)
    }

}
