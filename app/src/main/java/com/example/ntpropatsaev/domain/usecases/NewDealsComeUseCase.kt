package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.repository.Repository

class NewDealsComeUseCase(
    private val repository: Repository
) {

    operator fun invoke() {
        repository.newDealsCome()
    }
}