package com.clean.baseinstrumentationapp

import com.clean.BaseApplication
import com.clean.testdi.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class InstrumentationTestApp : BaseApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerTestAppComponent.builder().application(this).build()
    }
}