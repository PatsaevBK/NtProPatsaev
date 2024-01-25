package com.example.ntpropatsaev.presentation.main

import com.example.ntpropatsaev.domain.entity.MyDeal
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown

sealed class MainScreenState {
    data object Loading: MainScreenState()

    data class MyDealsState(
        val listOfMyDeal: List<MyDeal>,
        val sortOrder: SortOrder,
        val upDown: UpDown
    ): MainScreenState()
}