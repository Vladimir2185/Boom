package com.practicum.boom.home.holidays

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.boom.MainActivity
import com.practicum.boom.MainViewModel
import com.practicum.boom.R
import com.practicum.boom.home.MainHomeFragment
import com.practicum.boom.home.BaseProductAdapter
import com.practicum.boom.myCustomClasses.CustomGridLayoutManager
import com.practicum.boom.myCustomClasses.GeneralAdapterRV
import com.practicum.boom.myCustomClasses.GeneralBaseFragment
import kotlinx.android.synthetic.main.fragment_base_general.*


class FragmentHome3 : GeneralBaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome3()
    }

    override val TYPE = "tires"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}