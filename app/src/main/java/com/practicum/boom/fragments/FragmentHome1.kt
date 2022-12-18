package com.practicum.boom.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
    private var lockToRecView = false
    private var recyclerViewTouchBlock = true
    private var scrollViewTouchBlock = false
    private var oldScrollY = 0.0f
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
            ) {Log.i("test", "scrollY: " + scrollY)
                if (lock==false&&hightPromoImage - scrollY <= 0 && oldScrollY < scrollY) {
                    scrollView_fragmentHome1.scrollY = hightPromoImage
                    if (lock == false) {
                        switchEventToRecView(true)

                        Log.i("test", "stop")
                    }
                }
                else if (lock&&oldScrollY > scrollY&&scrollView_fragmentHome1.scrollY<hightPromoImage)
                {switchEventToRecView(false)
                    Log.i("test", "speed")

                   // scrollView_fragmentHome1.smoothScrollTo(0,150)

                }
                else if(oldScrollY > scrollY&&scrollView_fragmentHome1.scrollY<hightPromoImage){scrollView_fragmentHome1.scrollY = (hightPromoImage*0.8).toInt()}
            }
        })
        //when recyclerView scrolling reached top and canScrollVertically=false
        //switching recyclerViewTouchBlock and scrollViewTouchBlock and  lock
        //in switchEventToRecView transferring control of event from recyclerView to scrollView
        recyclerView_fragmentHome1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(-1) && lock) {
                    //switchEventToRecView(false)
                    Log.i("test", "test " + motionEvent)
                }
            }
        })

        scrollView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                event?.let { motionEvent = it }
                if (motionEvent.action == MotionEvent.ACTION_UP && lock == false) {
                    //closer()

                }
                Log.i("test", "scrollView: " + motionEvent)

                //InterceptTouchEvent false by default
                return false// scrollViewTouchBlock
            }
        })
        recyclerView_fragmentHome1.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                event?.let { motionEvent = it }
                Log.i("test", "recyclerView: " + motionEvent)
                Log.i("test", "recyclerView isInTouchMode: " + recyclerView_fragmentHome1.isInTouchMode)
                if (motionEvent.action == MotionEvent.ACTION_UP && lock == true) {
                    //closer()
                    Log.i("test2", "catch true")
                }
                //InterceptTouchEvent true by default
                return false//recyclerViewTouchBlock
            }
        })


        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                //Log.i("test", "recyclerView: ")
            }
        }
        mainViewModel.liveScrollPromoImage.observe(viewLifecycleOwner, {
            textView_fragmentHome1.text = it.toString()
        })
    }


    // transferring control of event from recyclerView to scrollView and vice versa
    fun switchEventToRecView(actionDown: Boolean) {
        lock = !lock
       // recyclerViewTouchBlock = !recyclerViewTouchBlock
        //scrollViewTouchBlock = !scrollViewTouchBlock
        if (actionDown){ motionEvent.action = MotionEvent.ACTION_DOWN
            recyclerView_fragmentHome1.isNestedScrollingEnabled=true
            recyclerViewTouchBlock = !recyclerViewTouchBlock
            scrollView_fragmentHome1.dispatchTouchEvent(motionEvent)
            }
        else {
            recyclerView_fragmentHome1.isNestedScrollingEnabled=false
            recyclerViewTouchBlock = !recyclerViewTouchBlock
            motionEvent.action = MotionEvent.ACTION_UP
            scrollView_fragmentHome1.dispatchTouchEvent(motionEvent)




        }

    }
    //smoothly brings to the position
    fun closer() {
        if (scrollView_fragmentHome1.scrollY > hightPromoImage / 2)
            scrollView_fragmentHome1.scrollY= hightPromoImage
        else scrollView_fragmentHome1.scrollY=0
    }

    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}