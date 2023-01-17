package com.practicum.boom.boom.home

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.boom.boom.home.sale.ProductAdapterFH2

class VP2Adapter(fragAct: FragmentActivity, private val fragList: List<Fragment>) :
    FragmentStateAdapter(fragAct) {

    override fun getItemCount(): Int {

        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragList[position]

    }

}