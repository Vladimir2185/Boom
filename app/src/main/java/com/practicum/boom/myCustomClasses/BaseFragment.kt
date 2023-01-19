package com.practicum.boom.myCustomClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.boom.home.BaseProductAdapter
import com.practicum.boom.boom.home.promo.Promo
import kotlinx.android.synthetic.main.fragment_base.*

open class BaseFragment(private val type: String = "socks") : GeneralFragment() {


    override fun onDestroy() {
        Log.i("test4", "onDestroy() GBF")
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }


    // protected open fun setAdapter() = BaseProductAdapter(requireActivity(), NUMBER_OF_PROMO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (conLayout_fragmentBaseGeneral.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
//            marginTopRV()

        recyclerView = recycler_base

        productAdapter = BaseProductAdapter(requireActivity(), NUMBER_OF_PROMO)

        with(BaseProductAdapter) {

            recyclerView.adapter = productAdapter
            recyclerView.recycledViewPool.setMaxRecycledViews(VIEW_TYPE_PROMO, NUMBER_OF_PROMO)
            recyclerView.recycledViewPool.setMaxRecycledViews(VIEW_TYPE_UNEVEN, MAX_POOL_SIZE)
            recyclerView.recycledViewPool.setMaxRecycledViews(VIEW_TYPE_EVEN, MAX_POOL_SIZE)
        }
        getProduct(productAdapter)

        mainViewModel.liveOnResumeStatus.observe(viewLifecycleOwner) {
            onResume()
        }

        //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
        managerLayout =
            CustomGridLayoutManager(requireContext(), ScreenInfo().columnCount(), true)

        managerLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 5 is the sum of items in one repeated section
                return if (NUMBER_OF_PROMO > 0) {
                    when (position) {
                        in 0 until NUMBER_OF_PROMO -> ScreenInfo().columnCount()
                        else -> 1
                    }
                } else 1
            }
        }

        recyclerStatus()
        interfaceClick()

    }

    protected open fun getProduct(productAdapter: GeneralAdapterRV) {
        mainViewModel.getListOfProductsByType(type).observe(viewLifecycleOwner) {
            productAdapter.productList = it
        }
    }

}