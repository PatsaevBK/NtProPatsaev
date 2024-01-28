package com.example.ntpropatsaev.domain.entity

import androidx.paging.PagingData

sealed class DealsResult {

    class Success(
        val pagingData: PagingData<DealDomain>,
        val sortType: SortType,
        val sortOrder: SortOrder
    ) : DealsResult()

}