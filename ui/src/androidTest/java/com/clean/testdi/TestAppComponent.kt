package com.clean.testdi

import android.app.Application
import com.clean.BaseApplication
import com.clean.baseinstrumentationapp.InstrumentationTestApp
import com.clean.di.ActivityBuildersModule
import com.clean.di.ViewModelFactoryModule

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestApiModule::class, ViewModelFactoryModule::class, ActivityBuildersModule::class]
)
interface TestAppComponent : AndroidInjector<InstrumentationTestApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }
}