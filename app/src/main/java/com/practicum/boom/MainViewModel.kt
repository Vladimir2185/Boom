package com.practicum.boom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    val liveScrollSearch: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
}