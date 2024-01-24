package com.example.ntpropatsaev.presentation.main

import com.example.ntpropatsaev.domain.entity.MyDeal

sealed class MainScreenState {
    data object Initial: MainScreenState()

    data class MyDealsState(
        val listOfMyDeal: List<MyDeal>
    ): MainScreenState()
}