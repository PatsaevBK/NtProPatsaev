package com.example.ntpropatsaev.di

import android.content.Context
import com.example.ntpropatsaev.presentation.ViewModelFactory
import com.example.ntpropatsaev.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}