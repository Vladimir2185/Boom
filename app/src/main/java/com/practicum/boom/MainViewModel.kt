package com.practicum.boom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MainViewModel : ViewModel() {
    val liveScrollLock :MutableLiveData<Boolean> by lazy  {MutableLiveData<Boolean>()}


}