package com.practicum.boom.boom.home.best

import android.os.Bundle
import android.view.View
import com.practicum.boom.boom.home.BaseProductAdapter
import com.practicum.boom.myCustomClasses.BaseFragment
import com.practicum.boom.myCustomClasses.GeneralAdapterRV


class FragmentHome1() : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome1()
    }

    override val NUMBER_OF_PROMO = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getProduct(productAdapter: GeneralAdapterRV) {
        mainViewModel.getBestProduct().observe(viewLifecycleOwner) {
            productAdapter.productList = it
        }
    }
}