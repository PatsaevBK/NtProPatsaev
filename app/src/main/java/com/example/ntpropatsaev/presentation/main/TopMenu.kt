package com.example.ntpropatsaev.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ntpropatsaev.R
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenu(
    mainScreenState: State<MainScreenState>,
    onSortOrderClickListener: (SortOrder) -> Unit,
    onUpDownClickListener: (UpDown) -> Unit
) {
    val screenState = mainScreenState.value
    val sortOrder = when (screenState) {
        MainScreenState.Loading -> SortOrder.DATA_CHANGE
        is MainScreenState.MyDealsState -> screenState.sortOrder
    }
    val iconDesc = when (screenState) {
        MainScreenState.Loading -> R.drawable.baseline_arrow_downward_24
        is MainScreenState.MyDealsState -> {
            if (screenState.upDown == UpDown.UP) {
                R.drawable.baseline_arrow_upward_24
            } else R.drawable.baseline_arrow_downward_24
        }
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val orders = remember {
        SortOrder.entries
    }
    var selectedOrder by remember {
        mutableStateOf(sortOrder.nameOfOrder)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedOrder,
                onValueChange = {
                    selectedOrder = it
                },
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()

            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                orders.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.nameOfOrder) },
                        onClick = {
                            expanded = false
                            selectedOrder = it.nameOfOrder
                            onSortOrderClickListener(it)
                        }
                    )
                }
            }
        }
        IconButton(onClick = {
            val next =
                if (iconDesc == R.drawable.baseline_arrow_downward_24) UpDown.UP else UpDown.DOWN
            onUpDownClickListener(next)
        }) {
            Icon(painter = painterResource(id = iconDesc), contentDescription = null)
        }
    }

}

