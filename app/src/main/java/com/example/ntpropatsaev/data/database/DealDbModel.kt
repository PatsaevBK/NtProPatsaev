package com.example.ntpropatsaev.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ntpropatsaev.domain.entity.DealDomain

@Entity("full_deals_list")
data class DealDbModel(
    @PrimaryKey
    val id: Long,
    val date: Long,
    val instrumentName: String,
    val price: Double,
    val amount: Double,
    val side: String
)