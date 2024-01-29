package com.example.ntpropatsaev.data.database

import androidx.room.TypeConverter
import com.example.ntpropatsaev.data.server.Server

class SideConverter {

    @TypeConverter
    fun sideToString(side: Server.DealDto.Side) = side.toString()

    @TypeConverter
    fun stringToSide(side: String) = when (side) {
        Server.DealDto.Side.BUY.toString() -> Server.DealDto.Side.BUY
        Server.DealDto.Side.SELL.toString() -> Server.DealDto.Side.SELL
        else -> throw IllegalArgumentException("There is no such Type side = $side")
    }
}