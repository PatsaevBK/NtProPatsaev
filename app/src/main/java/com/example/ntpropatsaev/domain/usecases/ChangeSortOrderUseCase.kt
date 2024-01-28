package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.repository.Repository

class ChangeSortOrderUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(sortType: SortType) {
        repository.changeSortType(sortType = sortType)
    }
}