package com.example.ntpropatsaev.data.mapper

import com.example.ntpropatsaev.data.database.DealDbModel
import com.example.ntpropatsaev.data.server.Server
import com.example.ntpropatsaev.domain.entity.DealDomain
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor



fun List<Server.Deal>.mapDealToDealDbModel() = this.map {
    DealDbModel(
        id = it.id,
        date = it.timeStamp.time,
        instrumentName = it.instrumentName,
        price = it.price,
        amount = it.amount,
        side = it.side.name
    )
}

fun List<DealDbModel>.mapDealDbModelToDealDomain() = this.map {
    DealDomain(
        id = it.id,
        date = it.date.convertTimestampToTime(),
        instrumentName = it.instrumentName,
        price = ceil(it.price * 100) / 100,
        amount = floor(it.amount),
        side = it.side.convertSide()
    )
}

private fun Long.convertTimestampToTime(): String {
    val date = Date(this)
    val pattern = "dd.mm.yy HH:mm"
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

private fun Server.Deal.Side.convertSide(): DealDomain.Side {
    return when (this) {
        Server.Deal.Side.SELL -> DealDomain.Side.SELL
        Server.Deal.Side.BUY -> DealDomain.Side.BUY
    }
}

private fun String.convertSide(): DealDomain.Side {
    return when (this) {
        DealDomain.Side.SELL.name -> DealDomain.Side.SELL
        DealDomain.Side.BUY.name -> DealDomain.Side.BUY
        else -> throw IllegalStateException("Don't know what a side $this")
    }
}