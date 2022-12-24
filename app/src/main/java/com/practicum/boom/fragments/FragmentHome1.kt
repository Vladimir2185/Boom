package com.practicum.boom.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.*
import com.practicum.boom.adapters.ProductAdapter
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.main_home_fragment.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var oldDY = 0
    private var lock = false
    var onScrollMove: OnScrollMove? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(mainViewModel.liveScrollLock) {
            observe(viewLifecycleOwner, {
                lock = it
            })

            val productAdapter = context?.let { ProductAdapter(it, screenInfo) }
            recyclerView_fragmentHome1.adapter = productAdapter

            //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
            val layoutManager =
                CustomGridLayoutManager(requireContext(), screenInfo.columnCount(), true)

            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // 5 is the sum of items in one repeated section
                    return when (position) {
                        0 -> screenInfo.columnCount()
                        else -> 1
                    }
                }
            }
            recyclerView_fragmentHome1.layoutManager = layoutManager


            recyclerView_fragmentHome1.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if ((oldDY!=0&&oldDY * dy < 0)) {
                        value = false
                        layoutManager.enable = false

                    }
                    oldDY=dy
                }
            })

            recyclerView_fragmentHome1.addOnItemTouchListener(object :
                RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                    value?.let { layoutManager.enable = it }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })


            productAdapter?.onFragmentClickListener =
                object : ProductAdapter.OnFragmentClickListener {
                    override fun onFragmentClick() {
                    }
                }
        }
    }

    interface OnScrollMove {
        fun onScroll(dy: Int) {}
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }

}