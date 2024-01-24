package com.example.ntpropatsaev.domain.repository

import com.example.ntpropatsaev.domain.entity.DealsResult
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun getDeals(): StateFlow<DealsResult>

    fun newDealsCome()
}