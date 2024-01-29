package com.example.ntpropatsaev.presentation.main

import androidx.compose.runtime.Immutable
import com.example.ntpropatsaev.domain.entity.Deal
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder

sealed class MainScreenState {
    data object Loading: MainScreenState()

    @Immutable
    data class Success(
        val listOfDeal: List<Deal>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ): MainScreenState()
}