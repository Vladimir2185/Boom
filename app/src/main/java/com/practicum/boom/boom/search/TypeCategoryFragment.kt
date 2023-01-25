package com.practicum.boom.boom.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.api.ShopInfo
import com.practicum.boom.boom.home.BaseProductAdapter
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import com.practicum.boom.myCustomClasses.GeneralDetailFragment
import kotlinx.android.synthetic.main.base_detail2.*
import kotlinx.android.synthetic.main.fragment_base.*


class TypeCategoryFragment(private val offsetPosition: Int, private val shopInfo: ShopInfo) :
    GeneralDetailFragment() {
    private val NUMBER_OF_PROMO = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.base_detail2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        textTitleType.text = shopInfo.title
        layoutInflater.inflate(R.layout.fragment_base, conLayoutAttachTo_baseDetail2, true)

        val recyclerView = recycler_base

        conLayoutAttachTo_baseDetail2.doOnLayout {
            val h = it.y.toInt()
            recyclerView.doOnLayout {
                it.layoutParams.height =
                    ScreenInfo().heightInPixels - h - 8 * ScreenInfo().screenDensity.toInt()
            }
        }

        val productAdapter = TypeCategoryAdapter(requireActivity(), NUMBER_OF_PROMO)

        recyclerView.adapter = productAdapter
        val managerLayout =
            GridLayoutManager(requireContext(), ScreenInfo().columnCount())



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
        recyclerView.layoutManager = managerLayout
        mainViewModel.getListOfProductsByType("desktop").observe(viewLifecycleOwner) {
            productAdapter.productList = it

            mainViewModel.getInfoByType("type").observe(viewLifecycleOwner) {
                productAdapter.shopInfoList = it
            }

        }
//        mainViewModel.getInfoByType("type").observe(viewLifecycleOwner) {
//            productAdapter.shopInfoList = it
//        }
    }
}