package com.practicum.boom.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.*
import com.practicum.boom.adapters.ProductAdapter
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
            var consume=false
            Handler(Looper.getMainLooper()).postDelayed({
                consume=!consume
            }, 2000)
            recyclerView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

                    Log.i("test", "MotionEvent " +p1)
                      if(p1?.action == MotionEvent.ACTION_DOWN)consume=true
                      else if (p1?.action == MotionEvent.ACTION_MOVE)consume=true
                      else consume= false
                      return  consume
                }
            })

            recyclerView_fragmentHome1.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    /* if (dy * oldDY < 0) {
                         recyclerView_fragmentHome1.smoothScrollBy(0, -dy)
                         value = false
                     }
                     oldDY = dy */
                    /* if ((!lock && dy < 0) )//|| (lock && dy < 0))
                     {
                        // recyclerView_fragmentHome1.scrollBy(0, -dy)
                         onScrollMove?.onScroll(dy * 2.5.toInt())

                     } */
                }
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