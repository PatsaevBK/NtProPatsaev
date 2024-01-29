package com.example.ntpropatsaev.domain.entity

sealed class DealsResult {

    class Success(
        val listDealDbModel: List<Deal>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ) : DealsResult()

    data object Error : DealsResult()

    data object Idle : DealsResult()
}