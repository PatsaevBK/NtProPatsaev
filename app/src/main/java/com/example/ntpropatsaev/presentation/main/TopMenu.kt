package com.example.ntpropatsaev.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ntpropatsaev.R
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenu(
    mainScreenState: State<MainScreenState>,
    onSortOrderClickListener: (SortType) -> Unit,
    onUpDownClickListener: (SortOrder) -> Unit
) {
    val screenState = mainScreenState.value
    val sortType = when (screenState) {
        MainScreenState.Loading -> SortType.DATE_CHANGE
        is MainScreenState.Success -> screenState.sortType
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val orders = remember {
        SortType.entries
    }
    var selectedOrder by remember {
        mutableStateOf(sortType.nameOfOrder)
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
        ChangeSortOrderButton(
            mainScreenState = screenState,
            onUpDownClickListener = onUpDownClickListener
        )
    }

}

@Composable
private fun ChangeSortOrderButton(
    mainScreenState: MainScreenState,
    onUpDownClickListener: (SortOrder) -> Unit
) {
    var sortOrder by remember {
        mutableStateOf(SortOrder.DESC)
    }
    var iconId by remember {
        mutableIntStateOf(R.drawable.baseline_arrow_downward_24)
    }
    when (mainScreenState) {
        MainScreenState.Loading -> {}
        is MainScreenState.Success -> {
            sortOrder = mainScreenState.sortOrder
            iconId = if (sortOrder == SortOrder.ASC) {
                R.drawable.baseline_arrow_upward_24
            } else R.drawable.baseline_arrow_downward_24
        }
    }
    FilledIconButton(onClick = {
        val next = if (sortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
        onUpDownClickListener(next)
    }) {
        Icon(
            painter = painterResource(
                id = iconId
            ),
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
    }
}

@Preview
@Composable
fun PreviewTopBar() {
    TopMenu(
        mainScreenState = remember { mutableStateOf(MainScreenState.Loading) },
        onSortOrderClickListener = {},
        onUpDownClickListener = {})
}

