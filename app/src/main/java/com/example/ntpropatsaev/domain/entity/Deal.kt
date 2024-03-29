package com.example.ntpropatsaev.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Deal(
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
