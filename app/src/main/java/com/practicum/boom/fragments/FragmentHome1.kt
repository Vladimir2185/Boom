package com.practicum.boom.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.*
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.main_home_fragment.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var lock = false
    private lateinit var motionEvent: MotionEvent
    var onSwitchNestedScroll: OnSwitchNestedScroll? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mainViewModel.liveScrollData) {
            observe(viewLifecycleOwner, {
                recyclerView_fragmentHome1.isNestedScrollingEnabled = it.isNested
                lock = it.lock

            })

            val productAdapter = context?.let { ProductAdapter(it, screenInfo) }
            recyclerView_fragmentHome1.adapter = productAdapter

            //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
            recyclerView_fragmentHome1.layoutManager =
                CustomGridLayoutManager(requireContext(), screenInfo.columnCount(), true)


            recyclerView_fragmentHome1.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.i("test", "RecyclerView.OnScroll "+dy)
                    Log.i("test", "lock "+lock)
                    Log.i("test", "isNestedScroll "+recyclerView_fragmentHome1.isNestedScrollingEnabled)
                    if (lock && dy < 0) {
                        recyclerView_fragmentHome1.scrollBy(0, -dy)
                        onSwitchNestedScroll?.onSwitch()
                    }
                }
            })


            recyclerView_fragmentHome1.addOnItemTouchListener(object :
                RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                    motionEvent = event
                    if (motionEvent.action == MotionEvent.ACTION_MASK && !lock) {
                        //switchNestedScroll(false)

                        Log.i("test", "onInterceptTouch ")
                    }
                    if (motionEvent.action == MotionEvent.ACTION_UP && !lock
                    ) {
                        //closer()
                    }
                    Log.i("test", "recViewINTERCEPT: " + motionEvent)
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
            productAdapter?.onFragmentClickListener =
                object : ProductAdapter.OnFragmentClickListener {
                    override fun onFragmentClick() {
                        Log.i("test", "recyclerclick: ")
                    }
                }
        }
    }

    interface OnSwitchNestedScroll {
        fun onSwitch()
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}