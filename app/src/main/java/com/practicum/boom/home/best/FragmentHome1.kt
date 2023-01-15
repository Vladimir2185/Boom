package com.practicum.boom.home.best

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainViewModel
import com.practicum.boom.MainViewModel.Companion.type
import com.practicum.boom.R
import com.practicum.boom.home.BaseProductAdapter
import com.practicum.boom.home.promo.Promo
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.practicum.boom.myCustomClasses.GeneralBaseFragment
import kotlinx.android.synthetic.main.fragment_base_general.*


class FragmentHome1() : GeneralBaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome1()
    }

    override val NUMBER_OF_PROMO = 1
    override val TYPE = "socks"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getProduct(baseProductAdapter: BaseProductAdapter) {
        mainViewModel.getBestProduct().observe(viewLifecycleOwner) {
            baseProductAdapter.productList = it
        }
    }
}