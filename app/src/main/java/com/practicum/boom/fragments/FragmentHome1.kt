package com.practicum.boom.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.*
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var h: Int = 0
    private var lock = false
    private var recyclerViewTouchBlock = true
    private var scrollViewTouchBlock = false
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

        //layout topMargin of promoImage
        val marginTop =
            (promoImage_fragmentHome1.layoutParams as ViewGroup.MarginLayoutParams)

        //return height of view element
        promoImage_fragmentHome1.doOnLayout {
            h = it.height

        }



        scrollView_fragmentHome1.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(
                v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int
            ) {
                // if (oldScrollY > scrollY) { lock = false }

                if (h - scrollY <= 0) {
                    scrollView_fragmentHome1.scrollY = h + marginTop.topMargin
                    Log.i("test", "ytrtyry")
                    if (lock == false) {
                        lock = true

                        //scrollViewTouchBlock = true
                       // recyclerViewTouchBlock = false
                    }
                }
            }
        })
        scrollView_fragmentHome1.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                Log.i("test", "onTouch: " + event)


                return scrollViewTouchBlock
            }

        })
//blocking touch of recyclerView until promoImage is hide
        recyclerView_fragmentHome1.addOnItemTouchListener(object :
            RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {


                Log.i("test", "onInterceptTouchEvent " + e)

                return  false//recyclerViewTouchBlock
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {Log.i("test", "onInterceptTouchEvent2 " + e)}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {Log.i("test", "onInterceptTouchEvent3 " )}
        })

        recyclerView_fragmentHome1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })
        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                //  recyclerView_fragmentHome1.suppressLayout(false)

            }

        }


        mainViewModel.liveScrollPromoImage.observe(viewLifecycleOwner, {
            textView_fragmentHome1.text = it.toString()
        })

    }


    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}