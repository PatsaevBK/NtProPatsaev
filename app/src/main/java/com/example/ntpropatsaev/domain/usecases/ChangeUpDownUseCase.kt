package com.example.ntpropatsaev.domain.usecases

import com.example.ntpropatsaev.domain.entity.UpDown
import com.example.ntpropatsaev.domain.repository.Repository

class ChangeUpDownUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(upDown: UpDown) {
        repository.changeUpDown(upDown)
    }
}