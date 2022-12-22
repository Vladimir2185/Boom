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
    private var oldDY = 0


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
                    if (dy * oldDY < 0) {
                        recyclerView_fragmentHome1.smoothScrollBy(0, -dy)
                        value = false
                    }
                    oldDY = dy
                }
            })

            productAdapter?.onFragmentClickListener =
                object : ProductAdapter.OnFragmentClickListener {
                    override fun onFragmentClick() {
                        Log.i("test", "recyclerclick: ")
                    }
                }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}