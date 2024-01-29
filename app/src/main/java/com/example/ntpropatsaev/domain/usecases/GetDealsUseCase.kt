package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetDealsUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<DealsResult> {
        return repository.getDeals()
    }
}