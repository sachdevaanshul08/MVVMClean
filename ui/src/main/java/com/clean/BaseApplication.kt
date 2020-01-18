package com.clean

import android.content.Context
import com.clean.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


open class BaseApplication : DaggerApplication() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: BaseApplication
        fun applicationContext() = instance.applicationContext as Context
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

}


