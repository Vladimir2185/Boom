package com.practicum.boom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    val liveScrollPromoImage: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
}