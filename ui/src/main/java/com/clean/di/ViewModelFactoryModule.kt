package com.clean.di

import androidx.lifecycle.ViewModelProvider
import com.clean.mvvm.viewmodelsfactory.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}