package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.repository.Repository
import javax.inject.Inject

class ChangeSortOrderUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(sortType: SortType) {
        repository.changeSortType(sortType = sortType)
    }
}