package com.practicum.boom.myCustomClasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_DOWN
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.boom.home.BaseProductAdapter
import com.practicum.boom.boom.home.promo.Promo
import kotlinx.android.synthetic.main.fragment_base_general.*

open class GeneralBaseFragment(private val type: String = "socks") : Fragment() {
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected var scrollStatus = SCROLL_STATUS_DOWN
    protected var viewHolder: GeneralAdapterRV.CustomViewHolder? = null


    protected open val NUMBER_OF_PROMO = 0

    companion object {

        //fun marginTopRV() = (ScreenInfo().screenDensity * 8).toInt()
    }

    override fun onDestroy() {
        Log.i("test4", "onDestroy() GBF")
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_general, container, false)
    }


    override fun onResume() {
        viewHolder?.let { Promo.promoStart(it, requireContext()) }
        super.onResume()
    }


    // protected open fun setAdapter() = BaseProductAdapter(requireActivity(), NUMBER_OF_PROMO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (conLayout_fragmentBaseGeneral.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
//            marginTopRV()

        with(recycler_base) {

            val productAdapter = BaseProductAdapter(requireActivity(), NUMBER_OF_PROMO)

            with(BaseProductAdapter) {

                adapter = productAdapter
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_PROMO, NUMBER_OF_PROMO)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_UNEVEN, MAX_POOL_SIZE)
                recycledViewPool.setMaxRecycledViews(VIEW_TYPE_EVEN, MAX_POOL_SIZE)
            }
            getProduct(productAdapter)

            mainViewModel.liveOnResumeStatus.observe(viewLifecycleOwner) {
                onResume()
            }
            with(mainViewModel.liveScrollStatus) {
                observe(viewLifecycleOwner) {
                    scrollStatus = it
                }


                //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
                val layoutManager =
                    CustomGridLayoutManager(requireContext(), ScreenInfo().columnCount(), true)

                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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
                recycler_base.layoutManager = layoutManager

                addOnItemTouchListener(object :
                    RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                        value?.let { layoutManager.scrollStatus = it }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                })


                productAdapter.onFragmentListener =
                    object : GeneralAdapterRV.OnFragmentListener {
                        override fun onFavoriteSwitch(favorProduct: Boolean, prodID: String) {
                            mainViewModel.productUpdate(favorProduct, prodID)
                        }

                        override fun onPromoStart(holder: GeneralAdapterRV.CustomViewHolder) {
                            Promo.promoStart(holder, requireContext())
                            viewHolder = holder
                        }
                    }
            }
        }
    }

    protected open fun getProduct(productAdapter: BaseProductAdapter) {
        mainViewModel.getListOfProductsByType(type).observe(viewLifecycleOwner) {
            productAdapter.productList = it
        }
    }

}