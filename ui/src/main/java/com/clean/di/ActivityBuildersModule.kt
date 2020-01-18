package com.clean.di

import com.clean.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(modules = [HomeViewModelModule::class, FragmentBuildersModule::class])
    abstract fun injectMainActivity(): MainActivity
}