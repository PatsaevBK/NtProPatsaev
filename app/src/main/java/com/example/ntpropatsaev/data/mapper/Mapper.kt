package com.example.ntpropatsaev.data.mapper

import com.example.ntpropatsaev.data.database.DealDbModel
import com.example.ntpropatsaev.data.server.Server
import com.example.ntpropatsaev.domain.entity.Deal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor


fun List<Server.DealDto>.mapDealDtoToDealDbModel() = this.map {
    DealDbModel(
        id = it.id,
        date = it.timeStamp.time,
        instrumentName = it.instrumentName,
        price = it.price,
        amount = it.amount,
        side = it.side.name
    )
}

fun List<DealDbModel>.mapDealDbModelToDeal() = this.map {
    Deal(
        id = it.id,
        date = it.date.convertTimestampToTime(),
        instrumentName = it.instrumentName,
        price = ceil(it.price * 100) / 100,
        amount = floor(it.amount),
        side = it.side.convertSide()
    )
}

fun DealDbModel.mapDealDbModelToDeal() = Deal(
    id = this.id,
    date = this.date.convertTimestampToTime(),
    instrumentName = this.instrumentName,
    price = ceil(this.price * 100) / 100,
    amount = floor(this.amount),
    side = this.side.convertSide()
)

private fun Long.convertTimestampToTime(): String {
    val stamp = Timestamp(this)
    val date = Date(stamp.time)
    val pattern = "dd.MM.yy HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

private fun Server.DealDto.Side.convertSide(): Deal.Side {
    return when (this) {
        Server.DealDto.Side.SELL -> Deal.Side.SELL
        Server.DealDto.Side.BUY -> Deal.Side.BUY
    }
}

private fun String.convertSide(): Deal.Side {
    return when (this) {
        Deal.Side.SELL.name -> Deal.Side.SELL
        Deal.Side.BUY.name -> Deal.Side.BUY
        else -> throw IllegalStateException("Don't know what a side $this")
    }
}