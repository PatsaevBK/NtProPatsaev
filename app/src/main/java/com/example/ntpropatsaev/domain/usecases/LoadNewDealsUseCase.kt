package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.repository.Repository
import javax.inject.Inject

class LoadNewDealsUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() {
        repository.loadDeals()
    }
}