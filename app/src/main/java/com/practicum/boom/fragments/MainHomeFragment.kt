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
import com.practicum.boom.*
import com.practicum.boom.adapters.VP2Adapter
import kotlinx.android.synthetic.main.main_home_fragment.*


class MainHomeFragment(private val screenInfo: ScreenInfo) : Fragment() {
    private var textFragTitle = mutableListOf<String>()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var hightSearch = 0
    private var lock = false
    private var isNested = true
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

        with(mainViewModel.liveScrollLock) {
            observe(viewLifecycleOwner, {
                lock = it
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


            scrollView_main_home_fragment.onDispatchTouchEvent =
                object : CustomScrollView.OnDispatchTouchEvent {
                    override fun onDispatchTouch(): Boolean {
                        return lock
                    }
                }
            //when promoImage reached offset=(hightSearch + marginTop.topMargin)
            //switching recyclerViewTouchBlock and scrollViewTouchBlock and  lock
            //in switchEventToRecView transferring control of event from scrollView to recyclerView
            scrollView_main_home_fragment.setOnScrollChangeListener(object :
                View.OnScrollChangeListener {
                override fun onScrollChange(
                    v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
                ) {
                    with(scrollView_main_home_fragment) {
                        Log.i("test", "scrollViewscrollY: " + scrollY)
                        if (hightSearch - scrollY <= 0 && oldScrollY < scrollY) {
                            this.scrollY = hightSearch
                            value = true
                            Log.i("test", "stop")
                        }
                        if (oldScrollY > scrollY && this.scrollY == 0) {
                            value = true
                            Log.i("test", "speed")
                        }
                    }
                }
            })
            scrollView_main_home_fragment.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    event?.let {
                        motionEvent = it
                    }
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        closer()
                    }
                    Log.i("test", "scrollViewOnTouch: " + motionEvent)
                    return false
                }
            })
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

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = MainHomeFragment(screenInfo)
    }
}