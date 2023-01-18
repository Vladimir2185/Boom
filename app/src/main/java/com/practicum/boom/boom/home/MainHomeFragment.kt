package com.practicum.boom.boom.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.*
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_DOWN
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_MIDDLE
import com.practicum.boom.MainActivity.Companion.SCROLL_STATUS_UP
import com.practicum.boom.boom.home.best.FragmentHome1
import com.practicum.boom.boom.home.holidays.FragmentHome3
import com.practicum.boom.boom.home.promo.Promo
import com.practicum.boom.boom.home.sale.FragmentHome2
import com.practicum.boom.myCustomClasses.CustomScrollView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_home_fragment.*
import kotlinx.android.synthetic.main.main_search_fragment.*


class MainHomeFragment() : Fragment() {
    private var textFragTitle = mutableListOf<String>()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var triggerBorder = 0
    private var scrollStatus = SCROLL_STATUS_DOWN
    private val smoothCount = 20  // responsible for smoothness of closer
    private val closingTime = 8  // responsible for time of closing of closer


    companion object {
        @JvmStatic
        fun newInstance() = MainHomeFragment()

    }


    override fun onResume() {
        mainViewModel.liveOnResumeStatus.value =null
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchIcon =
            svSearch_mainHomeFragment.findViewById(androidx.appcompat.R.id.search_mag_icon) as ImageView
        searchIcon.setColorFilter(Color.rgb( 128, 128, 128))

        with(mainViewModel.liveScrollStatus) {
            observe(viewLifecycleOwner, {
                scrollStatus = it

            })

            tabLayout_home_fragment.doOnLayout {
                val tabLay = it.height
                requireActivity().mainWindow.doOnLayout {
                    vp2_mainHomeFragment.layoutParams.height = it.height - tabLay + 1
                }
            }
            cardSearch_mainHomeFragment.doOnLayout {
                triggerBorder = it.height +
                        (cardSearch_mainHomeFragment.layoutParams as ViewGroup.MarginLayoutParams).topMargin +
                        (tabLayout_home_fragment.layoutParams as ViewGroup.MarginLayoutParams).topMargin
            }

            val fragList = listOf(
                FragmentHome1.newInstance(),
                FragmentHome2.newInstance(),
                FragmentHome3.newInstance()
            )


            for (i in 0 until fragList.size) {
                textFragTitle.add(tabLayout_home_fragment.getTabAt(i)?.text.toString())
            }
            val vp2Adapter = VP2Adapter(requireActivity(), fragList)
            vp2_mainHomeFragment.adapter = vp2Adapter


            TabLayoutMediator(tabLayout_home_fragment, vp2_mainHomeFragment) { tab, pos ->
                tab.text = textFragTitle[pos]
            }.attach()
            //attaching image to tabItem3,because inbuilt set image cant change size of image
            tabLayout_home_fragment.getTabAt(2)?.customView = ivIconTrees_mainHomeFragment


            svSearch_mainHomeFragment.setOnQueryTextFocusChangeListener(object :
                View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    svSearch_mainHomeFragment.clearFocus()
                    requireActivity().bottomNavigationView.selectedItemId = R.id.item_2
                }
            })


            scrollView_mainHomeFragment.onDispatchTouchEvent =
                object : CustomScrollView.OnDispatchTouchEvent {
                    override fun onDispatchTouch(action_up: Boolean): Int {
                        // Log.i("test2", "scrollY " + scrollView_main_home_fragment.scrollY)
                        with(scrollView_mainHomeFragment) {
                            if (scrollY <= 0)
                                value = SCROLL_STATUS_DOWN
                            else if (scrollY >= triggerBorder) {
                                value = SCROLL_STATUS_UP
                                scrollY = triggerBorder
                            } else
                                value = SCROLL_STATUS_MIDDLE
                        }

                        if (action_up)
                            closer()
                        return scrollStatus
                    }
                }
        }
    }

    //smoothly brings to the position
    fun closer() {
        with(scrollView_mainHomeFragment) {
            if (scrollY > triggerBorder * 0.55 && scrollY < triggerBorder) {
                val temp = (triggerBorder - scrollY) / smoothCount
                for (i in 1..smoothCount) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (i == smoothCount && scrollY != triggerBorder - 1) scrollY =
                            triggerBorder - 1
                        else scrollY += temp
                    }, (closingTime * i).toLong())
                }
            }
            if (scrollY <= triggerBorder * 0.55) {
                val temp = scrollY / smoothCount
                for (i in 1..smoothCount) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (i == smoothCount && scrollY != 1) scrollY = 1
                        else scrollY -= temp
                    }, (closingTime * i).toLong())
                }
            }
        }
    }


}