package com.practicum.boom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ScrollView

open class CustomScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var oldScrollY = -1.0f
    private var lock = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (oldScrollY > it.y && it.action == MotionEvent.ACTION_MOVE && !lock) {
                it.action = MotionEvent.ACTION_MASK
                lock = true
            }
            if (oldScrollY < it.y) {
                lock = false
            }
            oldScrollY = it.y
        }
        //Log.i("test", "CustomScrollView2 : " + ev)
        return super.dispatchTouchEvent(ev)
    }
}
