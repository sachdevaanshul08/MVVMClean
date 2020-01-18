package com.clean.di

import com.clean.homescreen.HomeFragment
import com.clean.mapscreen.MapDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapDetailFragment
}