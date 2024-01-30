package com.example.ntpropatsaev.domain.repository

import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.SortType
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getDeals(): Flow<DealsResult>

    fun loadDeals()

    suspend fun changeSortType(sortType: SortType)

    suspend fun changeSortOrder(sortOrder: SortOrder)

    suspend fun clearDb()
}