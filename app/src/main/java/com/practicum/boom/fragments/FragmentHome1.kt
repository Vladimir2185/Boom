package com.practicum.boom.fragments

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable

import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.boom.R
import com.practicum.boom.ScreenInfo
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.item_product_info.*
import kotlinx.android.synthetic.main.main_home_fragment.*


class FragmentHome1(private val screenInfo: ScreenInfo) : Fragment() {

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
        recyclerView_fragmentHome1.layoutManager = GridLayoutManager(this.activity, screenInfo.columnCount())


        //return height of view element
        imageView.doOnLayout {

            var h = it.height.toDouble()
            h /= getSystem().displayMetrics.density//px -> dp

        }

    }



    companion object {
        @JvmStatic
        fun newInstance(screenInfo: ScreenInfo) = FragmentHome1(screenInfo)
    }


}