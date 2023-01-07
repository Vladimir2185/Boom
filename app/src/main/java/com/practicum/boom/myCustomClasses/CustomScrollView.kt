package com.practicum.boom.myCustomClasses

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.practicum.boom.MainViewModel
import com.practicum.boom.home.MainHomeFragment.Companion.SCROLL_STATUS_DOWN

open class CustomScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var scrollStatus = SCROLL_STATUS_DOWN
    private var oldEvent = -1
    var onDispatchTouchEvent: OnDispatchTouchEvent? = null


    interface OnDispatchTouchEvent {
        fun onDispatchTouch(action_up: Boolean): Int
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        ev?.let {
            onDispatchTouchEvent?.let {
                if (oldEvent != MotionEvent.ACTION_DOWN && ev.action == MotionEvent.ACTION_UP && scrollStatus==0) {
                    scrollStatus = it.onDispatchTouch(true)
                } else scrollStatus = it.onDispatchTouch(false)
            }
            oldEvent = it.action
        }

        return super.dispatchTouchEvent(ev)
    }

}
