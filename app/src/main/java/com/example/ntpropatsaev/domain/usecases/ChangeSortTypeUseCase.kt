package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.repository.Repository
import javax.inject.Inject

class ChangeSortTypeUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(sortOrder: SortOrder) {
        repository.changeSortOrder(sortOrder)
    }
}