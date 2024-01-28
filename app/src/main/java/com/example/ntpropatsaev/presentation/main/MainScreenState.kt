package com.example.ntpropatsaev.presentation.main

import androidx.paging.PagingData
import com.example.ntpropatsaev.domain.entity.DealDomain
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder

sealed class MainScreenState {
    data object Loading: MainScreenState()

    data class MyDealsState(
        val listOfDealDomain: PagingData<DealDomain>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ): MainScreenState()
}