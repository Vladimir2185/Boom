package com.practicum.boom.myCustomClasses

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity
import com.practicum.boom.MainViewModel
import com.practicum.boom.boom.home.BaseProductAdapter
import com.practicum.boom.boom.home.promo.Promo

open class GeneralFragment : Fragment() {
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected var scrollStatus = MainActivity.SCROLL_STATUS_DOWN
    protected var viewHolder: GeneralAdapterRV.CustomViewHolder? = null
    protected open val NUMBER_OF_PROMO = 0
    protected lateinit var productAdapter: GeneralAdapterRV
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var managerLayout: CustomGridLayoutManager

    override fun onResume() {
        viewHolder?.let { Promo.promoStart(it, requireContext()) }
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mainViewModel) {
            liveScrollStatus.observe(viewLifecycleOwner)
            {
                scrollStatus = it
            }

        }
    }

    protected fun recyclerStatus() {
        recyclerView.layoutManager = managerLayout

        recyclerView.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                mainViewModel.liveScrollStatus.value?.let { managerLayout.scrollStatus = it }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    protected fun interfaceClick() {
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