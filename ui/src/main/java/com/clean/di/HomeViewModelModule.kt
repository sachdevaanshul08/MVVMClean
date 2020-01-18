package com.clean.di

import androidx.lifecycle.ViewModel
import com.clean.mvvm.homescreen.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {
    @Binds
    @IntoMap
    @com.clean.di.ViewModelKey(HomeViewModel::class)
    abstract fun bindMyViewModel(homeViewModel: HomeViewModel): ViewModel
}