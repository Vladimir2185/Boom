package com.practicum.boom.fragments

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView

import androidx.core.view.doOnLayout
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.CustomGridLayoutManager
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlin.math.absoluteValue


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var h: Int = 0
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
            CustomGridLayoutManager(this.activity, screenInfo.columnCount(),false)


        val marginTopSearch =
            (promoImage_fragmentHome1.layoutParams as ViewGroup.MarginLayoutParams)

        //return height of view element
        promoImage_fragmentHome1.doOnLayout {
            h = it.height //- marginTopSearch.topMargin
            //h /= getSystem().displayMetrics.density//px -> dp
        }

        recyclerView_fragmentHome1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /* if (h-marginTopSearch.topMargin.absoluteValue>=dy) {
                     marginTopSearch.topMargin -= dy
                 } else {
                     marginTopSearch.topMargin = -h
                 }
                 recyclerView_fragmentHome1.adapter?.notifyDataSetChanged()*/
            }
        })
        productAdapter?.onFragmentClickListener = object : ProductAdapter.OnFragmentClickListener {
            override fun onFragmentClick() {
                 Log.i("test "," 56758 ")
                if (h - marginTopSearch.topMargin.absoluteValue >= 50) {
                    marginTopSearch.topMargin -= 50
                } else {
                    marginTopSearch.topMargin = -h
                    recyclerView_fragmentHome1.layoutManager =
                        CustomGridLayoutManager(requireContext(), screenInfo.columnCount(),true)
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