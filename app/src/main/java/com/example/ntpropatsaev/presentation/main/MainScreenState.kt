package com.example.ntpropatsaev.presentation.main

import com.example.ntpropatsaev.domain.entity.DealDomain
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder

sealed class MainScreenState {
    data object Loading: MainScreenState()

    data class MyDealsState(
        val listOfDealDomain: List<DealDomain>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ): MainScreenState()
}