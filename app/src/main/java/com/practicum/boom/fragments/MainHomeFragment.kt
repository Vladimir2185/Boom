package com.practicum.boom.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.ScrollData
import com.practicum.boom.adapters.VP2Adapter
import kotlinx.android.synthetic.main.main_home_fragment.*


class MainHomeFragment(private val screenInfo: ScreenInfo) : Fragment() {
    private var textFragTitle = mutableListOf<String>()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var hightSearch = 0
    private var lock = false
    private var isNested = false
    private lateinit var motionEvent: MotionEvent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mainViewModel.liveScrollData) {
            observe(viewLifecycleOwner, {
                lock = it.lock

            })
            cardView_home_fragment.doOnLayout {
                hightSearch = it.height +
                        (cardView_home_fragment.layoutParams as ViewGroup.MarginLayoutParams).topMargin +
                        (tabLayout_home_fragment.layoutParams as ViewGroup.MarginLayoutParams).topMargin
            }
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
            tabLayout_home_fragment.getTabAt(2)?.customView = ivIconTrees_home_fragment

            //when promoImage reached offset=(hightSearch + marginTop.topMargin)
            //switching recyclerViewTouchBlock and scrollViewTouchBlock and  lock
            //in switchEventToRecView transferring control of event from scrollView to recyclerView
            scrollView_main_home_fragment.setOnScrollChangeListener(object :
                View.OnScrollChangeListener {
                override fun onScrollChange(
                    v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
                ) {
                    Log.i("test", "scrollViewscrollY: " + scrollY)
                    if (!lock && hightSearch - scrollY <= 0 && oldScrollY < scrollY) {
                        scrollView_main_home_fragment.scrollY = hightSearch
                        if (!lock) {
                            switchNestedScroll()
                            Log.i("test", "stop")
                        }
                    } else if (lock && oldScrollY > scrollY) {
                        value?.lock = !lock
                        Log.i("test", "speed")
                    }
                }
            })
            scrollView_main_home_fragment.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    event?.let {
                        motionEvent = it
                    }
                    if (motionEvent.action == MotionEvent.ACTION_UP && !lock
                    ) {
                        closer()
                    }
                    Log.i("test", "scrollViewOnTouch: " + motionEvent)
                    return false
                }
            })

            (fragList[0] as FragmentHome1).onSwitchNestedScroll =
                object : FragmentHome1.OnSwitchNestedScroll {
                    override fun onSwitch() {
                        Log.i("test", "INTERFACE: ")
                        switchNestedScroll()
                    }
                }
        }
    }

    //smoothly brings to the position
    fun closer() {
        with(scrollView_main_home_fragment) {
            if (scrollY > hightSearch * 0.55 && scrollY < hightSearch)
                scrollY = hightSearch
            else if (scrollY <= hightSearch * 0.55) scrollY = 0
        }
    }

    // transferring control of event from recyclerView to scrollView and vice versa
    fun switchNestedScroll() {

        if (lock) {
//            motionEvent.action = MotionEvent.ACTION_CANCEL
//            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)
//            motionEvent.action = MotionEvent.ACTION_DOWN
//            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)

            mainViewModel.liveScrollData.value = ScrollData(!lock, !isNested)
//            motionEvent.action = MotionEvent.ACTION_CANCEL
//            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)
//            motionEvent.action = MotionEvent.ACTION_DOWN
//            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)
        } else {
            motionEvent.action = MotionEvent.ACTION_CANCEL
            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)
            mainViewModel.liveScrollData.value = ScrollData(!lock, !isNested)
            motionEvent.action = MotionEvent.ACTION_DOWN
            scrollView_main_home_fragment.dispatchTouchEvent(motionEvent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = MainHomeFragment(screenInfo)
    }
}