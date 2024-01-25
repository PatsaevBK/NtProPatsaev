package com.example.ntpropatsaev.domain.entity

sealed class DealsResult {

    class Success(
        val listOfDeals: List<MyDeal>,
        val sortOrder: SortOrder,
        val upDown: UpDown
    ) : DealsResult()
}