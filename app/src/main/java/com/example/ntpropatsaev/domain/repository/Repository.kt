package com.example.ntpropatsaev.domain.repository

import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun getDeals(): StateFlow<DealsResult>

    fun loadDeals()

    suspend fun changeSortOrder(sortOrder: SortOrder)

    suspend fun changeUpDown(upDown: UpDown)
}