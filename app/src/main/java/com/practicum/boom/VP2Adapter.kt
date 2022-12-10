package com.practicum.boom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VP2Adapter(fragAct: FragmentActivity, private val fragList: List<Fragment>) :
    FragmentStateAdapter(fragAct) {
    override fun getItemCount(): Int {
        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }
}