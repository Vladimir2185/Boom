package com.practicum.boom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    val liveScrollData = MutableLiveData<ScrollData>()// by lazy { MutableLiveData<ScrollData>()


}