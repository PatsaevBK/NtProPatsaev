package com.example.ntpropatsaev.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntpropatsaev.domain.entity.MyDeal
import com.example.ntpropatsaev.ui.theme.NtProPatsaevTheme
import java.text.DecimalFormat

@Composable
fun DealCard(
    myDeal: MyDeal
) {
    Card {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column {
                Text(text = myDeal.instrumentName, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = myDeal.data)
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val color = when (myDeal.side) {
                    MyDeal.Side.SELL -> Color.Red
                    MyDeal.Side.BUY -> Color.Green
                }
                Text(text = "${myDeal.price} $", color = color, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${DecimalFormat("#").format(myDeal.amount)} шт.", color = Color.Gray)
            }
        }
    }
}

@Composable
@Preview
private fun PreviewCard() {
    val myDeal = MyDeal(
        0,
        "24.01.2024",
        "Alibaba",
        172.13,
        150.0,
        MyDeal.Side.BUY
    )
    NtProPatsaevTheme {
        DealCard(myDeal = myDeal)
    }
}