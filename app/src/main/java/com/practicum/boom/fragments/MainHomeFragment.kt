package com.practicum.boom.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.MainActivity
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.VP2Adapter
import kotlinx.android.synthetic.main.main_home_fragment.*


class MainHomeFragment(private val screenInfo: ScreenInfo) : Fragment() {
    private var textFragTitle = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val fragList = listOf(
            FragmentHome1.newInstance(screenInfo),
            FragmentHome2.newInstance(),
            FragmentHome3.newInstance()
        )

        for (i in 0 until fragList.size) {
            textFragTitle.add(tabLayoutHome.getTabAt(i)?.text.toString())
        }
        val vp2Adapter = VP2Adapter(requireActivity(), fragList)
        vp2Home.adapter = vp2Adapter
        TabLayoutMediator(tabLayoutHome, vp2Home) { tab, pos ->
            tab.text = textFragTitle[pos]
        }.attach()

        ivIconTrees.visibility = View.VISIBLE
        tabLayoutHome.getTabAt(2)?.customView = ivIconTrees
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = MainHomeFragment(screenInfo)
    }
}