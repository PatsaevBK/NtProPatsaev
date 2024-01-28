package com.example.ntpropatsaev.data.database

import androidx.room.TypeConverter
import com.example.ntpropatsaev.data.server.Server

class SideConverter {

    @TypeConverter
    fun sideToString(side: Server.Deal.Side) = side.toString()

    @TypeConverter
    fun stringToSide(side: String) = when (side) {
        Server.Deal.Side.BUY.toString() -> Server.Deal.Side.BUY
        Server.Deal.Side.SELL.toString() -> Server.Deal.Side.SELL
        else -> throw IllegalArgumentException("There is no such Type side = $side")
    }
}