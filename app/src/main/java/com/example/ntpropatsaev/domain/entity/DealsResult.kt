package com.example.ntpropatsaev.domain.entity

sealed class DealsResult {

    class Success(
        val listOfDeals: List<DealDomain>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ) : DealsResult()
}