package com.practicum.boom.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainViewModel
import com.practicum.boom.MainViewModel.Companion.type
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.ProductAdapter
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.item_product_info.*
import kotlinx.android.synthetic.main.item_promo.*


class FragmentHome1() : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome1()
    }

    //relative to width of icon
    private val HIGHT_OF_PRODUCT_ICON = 1.35
    private val mainViewModel: MainViewModel by activityViewModels()
    private var oldDY = 0
    private var lock = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(recyclerView_fragmentHome1) {
            //find-out screen resolution of current device and place it into screenInfo class
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenInfo =
                ScreenInfo(
                    displayMetrics.widthPixels,
                    displayMetrics.density,
                    HIGHT_OF_PRODUCT_ICON
                )

            val productAdapter = context?.let { ProductAdapter(it, screenInfo) }
            with(ProductAdapter) {
                adapter = productAdapter
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_PROMO, 1)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_UNEVEN, MAX_POOL_SIZE)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_EVEN, MAX_POOL_SIZE)
            }
//            mainViewModel.getAllListOfProducts().observe(viewLifecycleOwner, {
//                productAdapter?.productList = it
//            })
            mainViewModel.getListOfProductsByType(type).observe(viewLifecycleOwner, {
                productAdapter?.productList = it
            })
            with(mainViewModel.liveScrollLock) {
                observe(viewLifecycleOwner, {
                    lock = it
                })



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


                addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if ((oldDY != 0 && oldDY * dy < 0)) {
                            Log.i("test2", "onScrolled turn " + dy)
                            value = false
                            layoutManager.enable = false
                        }

                        oldDY = dy
                    }
                })

                addOnItemTouchListener(object :
                    RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        Log.i("test2", "onInterceptTouchEvent" + e)
                        value?.let { layoutManager.enable = it }
                        return false
                    }
                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                })


                productAdapter?.onFragmentClickListener =
                    object : ProductAdapter.OnFragmentClickListener {
                        override fun onFragmentClick() {}
                        override fun onFavoriteSwitch(favorProduct: Boolean, prodID: String) {
                            mainViewModel.productUpdate(favorProduct, prodID)
                        }
                    }
            }
        }
    }
}