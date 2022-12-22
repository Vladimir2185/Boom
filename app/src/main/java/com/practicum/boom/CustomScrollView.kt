package com.practicum.boom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.fragment.app.activityViewModels
import com.practicum.boom.fragments.FragmentHome1
import kotlinx.android.synthetic.main.main_home_fragment.*
import kotlinx.android.synthetic.main.main_home_fragment.view.*
import kotlin.math.absoluteValue

open class CustomScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    private var intercept = false
    private var lock = false
    private var lock2 = false
    private var freezeEvents = false
    private var oldEvent = -1
    var onDispatchTouchEvent: OnDispatchTouchEvent? = null
    private var newX = 0.0f
    private var oldX = 0.0f
    private var newY = 0.0f
    private var oldY = 0.0f

    interface OnDispatchTouchEvent {
        fun onDispatchTouch(action_up: Boolean): Boolean
    }

    fun horizontalMove(): Boolean {
        return ((newX - oldX).absoluteValue / (newY - oldY).absoluteValue + 0.00001) > 5
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (freezeEvents) return false

        ev?.let {
            newX = it.x
            newY = it.y
            onDispatchTouchEvent?.let {
                if (oldEvent != MotionEvent.ACTION_DOWN && ev.action == MotionEvent.ACTION_UP && !lock) {
                    lock = it.onDispatchTouch(true)
                    freezeEvents = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        freezeEvents = false
                    }, 200)
                } else lock = it.onDispatchTouch(false)
            }
            Log.i("test", "D lock " + lock + "   ev  " + ev)
            Log.i("test", "horizontalMove() " + horizontalMove())


            intercept = !lock
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
            if (it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_DOWN && !lock ) {
                intercept = super.onInterceptTouchEvent(ev)
            }
            oldX = newX
            oldY = newY
            oldEvent = it.action
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.i("test", "I lock " + !lock + "   ev  " + ev)
        return intercept
    }
}
