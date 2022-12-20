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
import com.practicum.boom.CustomGridLayoutManager
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val productAdapter = context?.let { ProductAdapter(it, screenInfo) }
        recyclerView_fragmentHome1.adapter = productAdapter

        //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
        recyclerView_fragmentHome1.layoutManager =
            CustomGridLayoutManager(this.activity, screenInfo.columnCount(), true)




      /*  recyclerView_fragmentHome1.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                motionEvent = event
                if (motionEvent.action == MotionEvent.ACTION_MASK && !lock) {
                    switchNestedScroll(false)
                    Log.i("test", "isNested FALSE ")
                }
                if (motionEvent.action == MotionEvent.ACTION_UP && !lock
                ) {
                    closer()
                }
                Log.i("test", "recViewINTERCEPT: " + motionEvent)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })*/
        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                Log.i("test", "recyclerclick: ")
                //mainViewModel.liveScrollSearch.value=210

            }
        }

    }



    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}