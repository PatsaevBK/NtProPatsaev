package com.example.ntpropatsaev.domain.entity

data class DealsResult(
    val listDealDbModel: List<Deal>,
    val sortType: SortType,
    val sortOrder: SortOrder
)