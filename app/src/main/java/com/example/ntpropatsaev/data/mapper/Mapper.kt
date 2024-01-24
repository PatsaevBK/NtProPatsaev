package com.example.ntpropatsaev.data.mapper

import com.example.ntpropatsaev.data.Server
import com.example.ntpropatsaev.domain.entity.MyDeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor

class Mapper {

    fun mapDealToMyDeal(deal: Server.Deal) = MyDeal(
        id = deal.id,
        data = convertTimestampToTime(deal.timeStamp),
        instrumentName = deal.instrumentName,
        price = ceil(deal.price * 100) / 100,
        amount = floor(deal.amount),
        side = convertSide(deal.side)
    )

    private fun convertTimestampToTime(timestamp: Date): String {
        val pattern = "dd.mm.yy HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(timestamp)
    }

    private fun convertSide(dealSide: Server.Deal.Side): MyDeal.Side {
        return when (dealSide) {
            Server.Deal.Side.SELL -> MyDeal.Side.SELL
            Server.Deal.Side.BUY -> MyDeal.Side.BUY
        }
    }
}