package com.example.ntpropatsaev.domain.entity

import androidx.compose.runtime.Immutable
import javax.inject.Inject

@Immutable
data class Deal @Inject constructor(
    val id: Long,
    val date: String,
    val instrumentName: String,
    val price: Double,
    val amount: Double,
    val side: Side,
) {
    enum class Side {
        SELL, BUY
    }
}
