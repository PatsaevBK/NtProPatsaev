package com.example.ntpropatsaev.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntpropatsaev.domain.entity.Deal
import com.example.ntpropatsaev.ui.theme.NtProPatsaevTheme
import java.text.DecimalFormat

@Composable
fun DealCard(
    deal: Deal
) {
    Card(
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(text = deal.instrumentName, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = deal.date)
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val color = when (deal.side) {
                    Deal.Side.SELL -> Color.Red
                    Deal.Side.BUY -> Color.Green
                }
                Text(text = "${deal.price} $", color = color, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${DecimalFormat("#").format(deal.amount)} шт.", color = Color.Gray)
            }
        }
    }
}

@Composable
@Preview
private fun PreviewCard() {
    val deal = Deal(
        0,
        "24.01.2024",
        "Alibaba",
        172.13,
        150.0,
        Deal.Side.BUY
    )
    NtProPatsaevTheme {
        DealCard(deal = deal)
    }
}