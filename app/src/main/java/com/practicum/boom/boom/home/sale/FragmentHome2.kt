package com.practicum.boom.boom.home.sale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.R
import com.practicum.boom.boom.home.promo.Promo
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.practicum.boom.myCustomClasses.GeneralFragment
import kotlinx.android.synthetic.main.fragment_home2.*


class FragmentHome2 : GeneralFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome2()
    }

    override val NUMBER_OF_PROMO = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getAllShopInfoList().observe(viewLifecycleOwner) {
            productAdapter.shopInfoList = it
        }

        recyclerView = recycler_fragmentHome2


        productAdapter = ProductAdapterFH2(requireActivity(), NUMBER_OF_PROMO)
        recyclerView.adapter = productAdapter


        managerLayout =
            CustomGridLayoutManager(requireContext(), 1, true)



        recyclerStatus()
        interfaceClick()

    }
}
