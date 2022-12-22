package com.practicum.boom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.fragment.app.activityViewModels
import com.practicum.boom.fragments.FragmentHome1
import kotlinx.android.synthetic.main.main_home_fragment.*
import kotlinx.android.synthetic.main.main_home_fragment.view.*

open class CustomScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var lock = false
    private var lock2 = false
    var onDispatchTouchEvent: OnDispatchTouchEvent? = null

    interface OnDispatchTouchEvent {
        fun onDispatchTouch(): Boolean
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        onDispatchTouchEvent?.let { lock = it.onDispatchTouch() }
        Log.i("test", "D lock " + lock)
        ev?.let {
            if (!lock) {
                if (lock2 == true) {
                    it.action = MotionEvent.ACTION_DOWN
                    onInterceptTouchEvent(it)
                    lock2 = !lock2
                }
            }
            if (lock) {
                if (lock2 == false) {
                    it.action = MotionEvent.ACTION_DOWN
                    onInterceptTouchEvent(it)
                    lock2 = !lock2
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        Log.i("test", "I lock " + !lock)

        return !lock
    }
}
