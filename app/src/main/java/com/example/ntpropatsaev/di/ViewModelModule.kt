package com.example.ntpropatsaev.di

import androidx.lifecycle.ViewModel
import com.example.ntpropatsaev.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModule(mainViewModel: MainViewModel): ViewModel
}