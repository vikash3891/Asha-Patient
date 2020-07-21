package com.home.asharemedy.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

open class BaseViewModel<N> : Observable() {
    var mNavigator: N? = null
}