package com.practicum.boom.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
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
            textFragTitle.add(tabLayout_home_fragment.getTabAt(i)?.text.toString())
        }
        val vp2Adapter = VP2Adapter(requireActivity(), fragList)
        vp2_home_fragment.adapter = vp2Adapter
        TabLayoutMediator(tabLayout_home_fragment, vp2_home_fragment) { tab, pos ->
            tab.text = textFragTitle[pos]
        }.attach()
//attaching image to tabItem3,because inbuilt set image cant change size of image
        ivIconTrees_home_fragment.visibility = View.VISIBLE
        tabLayout_home_fragment.getTabAt(2)?.customView = ivIconTrees_home_fragment
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = MainHomeFragment(screenInfo)
    }
}