package com.example.ntpropatsaev

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ntpropatsaev.di.ApplicationComponent
import com.example.ntpropatsaev.di.DaggerApplicationComponent

class NtProPatsaev: Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as NtProPatsaev).applicationComponent
}