package com.clean.mvvm.utils

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.forceRefresh() {
    this.value = this.value
}