package com.practicum.boom.home.holidays

import android.os.Bundle
import android.view.View
import com.practicum.boom.myCustomClasses.GeneralBaseFragment


class FragmentHome3 : GeneralBaseFragment("tires") {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHome3()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}