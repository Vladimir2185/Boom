package com.practicum.boom.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.*
import com.practicum.boom.home.best.FragmentHome1
import com.practicum.boom.home.holidays.FragmentHome3
import com.practicum.boom.home.sale.FragmentHome2
import com.practicum.boom.myCustomClasses.CustomScrollView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_home_fragment.*


class MainHomeFragment() : Fragment() {
    private var textFragTitle = mutableListOf<String>()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var hightSearch = 0
    private var scrollStatus = SCROLL_STATUS_DOWN
    private val smoothCount = 20  // responsible for smoothness of closer
    private val closingTime = 8  // responsible for time of closing of closer


    companion object {
        @JvmStatic
        fun newInstance() = MainHomeFragment()
        const val SCROLL_STATUS_UP = 1
        const val SCROLL_STATUS_MIDDLE = 0
        const val SCROLL_STATUS_DOWN = -1
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


        with(mainViewModel.liveScrollStatus) {
            observe(viewLifecycleOwner, {
                scrollStatus = it

            })

            tabLayout_home_fragment.doOnLayout {
                val tabLay = it.height
                requireActivity().mainWindow.doOnLayout {
                    vp2_home_fragment.layoutParams.height = it.height - tabLay + 1
                }
            }
            cardView_home_fragment.doOnLayout {
                hightSearch = it.height +
                        (cardView_home_fragment.layoutParams as ViewGroup.MarginLayoutParams).topMargin +
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
            vp2_home_fragment.adapter = vp2Adapter
            TabLayoutMediator(tabLayout_home_fragment, vp2_home_fragment) { tab, pos ->
                tab.text = textFragTitle[pos]
            }.attach()
            //attaching image to tabItem3,because inbuilt set image cant change size of image
            tabLayout_home_fragment.getTabAt(2)?.customView = ivIconTrees_home_fragment




            scrollView_main_home_fragment.onDispatchTouchEvent =
                object : CustomScrollView.OnDispatchTouchEvent {
                    override fun onDispatchTouch(action_up: Boolean): Int {
                        // Log.i("test2", "scrollY " + scrollView_main_home_fragment.scrollY)
                        with(scrollView_main_home_fragment) {
                            if (scrollY <= 0)
                                value = SCROLL_STATUS_DOWN
                            else if (scrollY >= hightSearch) {
                                value = SCROLL_STATUS_UP
                                scrollY = hightSearch
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
        with(scrollView_main_home_fragment) {
            if (scrollY > hightSearch * 0.55 && scrollY < hightSearch) {
                val temp = (hightSearch - scrollY) / smoothCount
                for (i in 1..smoothCount) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (i == smoothCount && scrollY != hightSearch - 1) scrollY =
                            hightSearch - 1
                        else scrollY += temp
                    }, (closingTime * i).toLong())
                }
            }
            if (scrollY <= hightSearch * 0.55) {
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