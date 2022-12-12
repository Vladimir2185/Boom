package com.practicum.boom.fragments

import android.content.res.Resources.getSystem
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.boom.R
import com.practicum.boom.adapters.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home1.*


class FragmentHome1(private val numCol: Int, private val heightProduct: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home1, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productAdapter = context?.let { ProductAdapter(it, numCol, heightProduct) }
        rvProductsBest.adapter = productAdapter
        rvProductsBest.layoutManager = GridLayoutManager(this.activity, numCol)
        var h = 1.0
        //return height of view element
        imageView.doOnLayout {
            Log.i("test", "onViewCreated " + it.height)
            h = it.height.toDouble()
            h /= getSystem().displayMetrics.density//px -> dp
            Log.i("test", "onViewCreated2 ${h.toInt()}")

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(num_col: Int, heightProduct: Int) = FragmentHome1(num_col, heightProduct)
    }


}