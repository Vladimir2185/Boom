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
                if (!lock && hightPromoImage - scrollY <= 0 && oldScrollY < scrollY) {
                    scrollView_fragmentHome1.scrollY = hightPromoImage
                    if (!lock) {
                        switchNestedScroll(true)
                        lock = !lock
                        Log.i("test", "stop")
                    }
                } else if (lock && oldScrollY > scrollY && scrollView_fragmentHome1.scrollY < hightPromoImage) {
                    lock = !lock
                    Log.i("test", "speed")
                }
            }
        })

        scrollView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                event?.let {
                    motionEvent = it
                }
                if (motionEvent.action == MotionEvent.ACTION_UP && !lock
                ) {
                    closer()
                }
                Log.i("test", "scrollView: " + motionEvent)
                return false
            }
        })

        recyclerView_fragmentHome1.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                motionEvent = event
                if (motionEvent.action == MotionEvent.ACTION_MASK && !lock) {
                    switchNestedScroll(false)
                    Log.i("test", "isNested FALSE ")
                }
                if (motionEvent.action == MotionEvent.ACTION_UP && !lock
                ) {
                    closer()
                }
                Log.i("test", "recViewINTERCEPT: " + motionEvent)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                Log.i("test", "recyclerclick: ")
            }
        }
        mainViewModel.liveScrollPromoImage.observe(viewLifecycleOwner, {
            textView_fragmentHome1.text = it.toString()
        })
    }

    // transferring control of event from recyclerView to scrollView and vice versa
    fun switchNestedScroll(isNested: Boolean) {
        motionEvent.action = MotionEvent.ACTION_CANCEL
        scrollView_fragmentHome1.dispatchTouchEvent(motionEvent)
        recyclerView_fragmentHome1.isNestedScrollingEnabled = isNested
        motionEvent.action = MotionEvent.ACTION_DOWN
        scrollView_fragmentHome1.dispatchTouchEvent(motionEvent)
    }

    //smoothly brings to the position
    fun closer() {
        with(scrollView_fragmentHome1) {
            if (scrollY > hightPromoImage * 0.60 && scrollY < hightPromoImage)
                scrollY = hightPromoImage
            else if (scrollY <= hightPromoImage * 0.60) scrollY = 0
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}