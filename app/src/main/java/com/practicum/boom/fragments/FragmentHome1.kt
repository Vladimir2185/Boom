package com.practicum.boom.fragments

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.view.doOnLayout
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var h: Int = 1
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
        recyclerView_fragmentHome1.layoutManager =
            GridLayoutManager(this.activity, screenInfo.columnCount())


        //return height of view element
        promoImage_fragmentHome1.doOnLayout {
            h = it.height
            //h /= getSystem().displayMetrics.density//px -> dp
        }
        var deltaY = 0
        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                textView_fragmentHome1.text = h.toString()

                if (deltaY <= h + 8 * 2.75) {
                    (promoImage_fragmentHome1.layoutParams as ViewGroup.MarginLayoutParams).topMargin -= 0
                    deltaY += 30
                }
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