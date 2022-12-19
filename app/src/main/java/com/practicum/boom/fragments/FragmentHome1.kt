package com.practicum.boom.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.*
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var hightPromoImage: Int = 0
    private var lock = false
    private var lockScrollGap = false
    private var lockOnScreenFinger = false
    private var recyclerViewTouchBlock = true
    private var scrollViewTouchBlock = false
    private var countGap = 0
    private var lockOnScreenFingerCount = 0
    private lateinit var motionEvent: MotionEvent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productAdapter = context?.let { ProductAdapter(it, screenInfo) }
        recyclerView_fragmentHome1.adapter = productAdapter

        //setup CustomGridLayoutManager and freezing/unfreezing recyclerView scrolling by isScrollEnabled param
        recyclerView_fragmentHome1.layoutManager =
            CustomGridLayoutManager(this.activity, screenInfo.columnCount(), true)

        //return height of view element
        promoImage_fragmentHome1.doOnLayout {
            hightPromoImage =
                it.height + (promoImage_fragmentHome1.layoutParams
                        as ViewGroup.MarginLayoutParams).topMargin
        }


        //when promoImage reached offset=(hightPromoImage + marginTop.topMargin)
        //switching recyclerViewTouchBlock and scrollViewTouchBlock and  lock
        //in switchEventToRecView transferring control of event from scrollView to recyclerView
        scrollView_fragmentHome1.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(
                v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                Log.i("test", "scrollY: " + scrollY)
                if (!lock  && hightPromoImage - scrollY <= 0 && oldScrollY < scrollY) {
                    scrollView_fragmentHome1.scrollY = hightPromoImage
                    if (!lock ) {
                       // switchEventToRecView(true)
                        //motionEvent.action = MotionEvent.ACTION_UP
                       // requireActivity().dispatchTouchEvent(motionEvent)
                        recyclerView_fragmentHome1.isNestedScrollingEnabled = true
                        motionEvent.action = MotionEvent.ACTION_DOWN
                        requireActivity().dispatchTouchEvent(motionEvent)
                        lock=!lock
                        Log.i("test", "stop")
                    }
                } else if (lock && oldScrollY > scrollY && scrollView_fragmentHome1.scrollY < hightPromoImage) {
                   // switchEventToRecView(false)
                    /*motionEvent.action = MotionEvent.ACTION_UP
                    requireActivity().dispatchTouchEvent(motionEvent)
                    recyclerView_fragmentHome1.isNestedScrollingEnabled = false
                    motionEvent.action = MotionEvent.ACTION_DOWN
                    requireActivity().dispatchTouchEvent(motionEvent)
                    lock=!lock*/
                    Log.i("test", "speed")

                }
                // scroll gap normalization
                if (oldScrollY > scrollY && lockScrollGap) {
                    if ((oldScrollY - scrollY > 20) && oldScrollY >= 20) {
                        scrollView_fragmentHome1.scrollY = oldScrollY - 20
                    }
                    countGap++
                    if (countGap == 3) {
                        lockScrollGap = false
                        countGap = 0
                    }
                }
            }
        })
        //when recyclerView scrolling reached top and canScrollVertically=false
        //switching recyclerViewTouchBlock and scrollViewTouchBlock and  lock
        //in switchEventToRecView transferring control of event from recyclerView to scrollView
        recyclerView_fragmentHome1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /*if (!recyclerView.canScrollVertically(-1) && lock) {
                    //switchEventToRecView(false)
                    Log.i("test", "test " + motionEvent)
                }*/
            }
        })

        scrollView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                event?.let { motionEvent = it }
                lockOnScreenFingerListener()

                if (!lockOnScreenFinger && motionEvent.action == MotionEvent.ACTION_UP
                    && !lock
                ) {
                    closer()
                }
                Log.i("test", "scrollView: " + motionEvent)
                return false
            }
        })
        recyclerView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                event?.let { motionEvent = it }
                lockOnScreenFingerListener()
                Log.i("test", "recyclerView: " + motionEvent)
                if (motionEvent.action == MotionEvent.ACTION_UP && lock ) {
                    lockScrollGap = true
                }
                return false
            }
        })
       // recyclerView_fragmentHome1.listener

        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {

            }
        }
        mainViewModel.liveScrollPromoImage.observe(viewLifecycleOwner, {
            textView_fragmentHome1.text = it.toString()
        })
    }


    // transferring control of event from recyclerView to scrollView and vice versa
    fun switchEventToRecView(actionDown: Boolean) {
        if (actionDown) {
            motionEvent.action = MotionEvent.ACTION_DOWN
            recyclerView_fragmentHome1.isNestedScrollingEnabled = true
        } else {

            motionEvent.action = MotionEvent.ACTION_UP
            recyclerView_fragmentHome1.isNestedScrollingEnabled = false
        }
        scrollView_fragmentHome1.dispatchTouchEvent(motionEvent)

        lock = !lock
        lockOnScreenFinger = true
    }

    //smoothly brings to the position
    fun closer() {
        with(scrollView_fragmentHome1) {
            if (scrollY > hightPromoImage * 0.6 && scrollY < hightPromoImage)
                scrollY = hightPromoImage
            else if (scrollY <= hightPromoImage * 0.6) scrollY = 0
        }
    }

    fun lockOnScreenFingerListener() {
        lockOnScreenFingerCount++
        if (lockOnScreenFingerCount >= 10) {
            lockOnScreenFingerCount = 0
            lockOnScreenFinger = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}