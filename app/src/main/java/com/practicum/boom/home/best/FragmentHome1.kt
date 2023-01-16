package com.practicum.boom.home.best

import android.os.Bundle
import android.view.View
import com.practicum.boom.home.BaseProductAdapter
import com.practicum.boom.myCustomClasses.GeneralBaseFragment


class FragmentHome1() : GeneralBaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome1()
    }

    override val NUMBER_OF_PROMO = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getProduct(baseProductAdapter: BaseProductAdapter) {
        mainViewModel.getBestProduct().observe(viewLifecycleOwner) {
            baseProductAdapter.productList = it
        }
    }
}