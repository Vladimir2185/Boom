package com.practicum.boom.boom.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnLayout
import com.practicum.boom.MainActivity.ScreenInfo
import com.practicum.boom.R
import com.practicum.boom.myCustomClasses.GeneralFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_search_fragment.*


class MainSearchFragment() : GeneralFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MainSearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchIcon =
            svSearch_mainSearchFragment.findViewById(androidx.appcompat.R.id.search_mag_icon) as ImageView
        searchIcon.setColorFilter(Color.rgb(128, 128, 128))



        svSearch_mainSearchFragment.setOnQueryTextFocusChangeListener(object :
            View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

                searchIcon.setImageResource(com.android.car.ui.R.drawable.car_ui_icon_arrow_back)
                searchIcon.setColorFilter(Color.rgb(128, 128, 128))
                searchIcon.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        svSearch_mainSearchFragment.clearFocus()
                        searchIcon.setImageResource(androidx.appcompat.R.drawable.abc_ic_search_api_material)
                        searchIcon.setOnClickListener(null)
                    }
                })
            }
        })
        cardSearch_mainSearchFragment.doOnLayout {
            val h = it.height
            requireActivity().bottomNavigationView.doOnLayout {
                val h1 = h + it.height
                conLayout_mainSearchFragment.doOnLayout {
                    it.layoutParams.height =
                        ScreenInfo().heightInPixels - h1 - 30 * ScreenInfo().screenDensity.toInt()
                }
            }
        }

        productAdapter = SearchAdapter(requireActivity(), NUMBER_OF_PROMO)
        recycler_mainSearchFragment.adapter = productAdapter

        mainViewModel.getInfoByType("type").observe(viewLifecycleOwner) {
            productAdapter.shopInfoList = it
        }
        interfaceClick()
    }
}