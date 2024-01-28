package com.example.ntpropatsaev.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ntpropatsaev.domain.entity.DealDomain
import com.example.ntpropatsaev.presentation.ViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun MainScreen() {
    val factory = remember { ViewModelFactory() }
    val mainViewModel: MainViewModel = viewModel(factory = factory)
    val mainScreenState = mainViewModel.screenState.collectAsState(
        initial = MainScreenState.Loading
    )
    Scaffold {
        Column {
            TopMenu(
                mainScreenState = mainScreenState,
                onSortOrderClickListener = {
                    mainViewModel.onSortOrderClick(it)
                },
                onUpDownClickListener = {
                    mainViewModel.onUpDownClick(it)
                }
            )
            MainScreenContent(
                mainScreenState = mainScreenState,
                paddingValues = it
            )
        }
    }

}

@Composable
fun MainScreenContent(
    mainScreenState: State<MainScreenState>,
    paddingValues: PaddingValues
) {
    when (val currentState = mainScreenState.value) {
        MainScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }

        is MainScreenState.MyDealsState -> {
            MyDealsShow(
                currentState,
                paddingValues
            )
        }
    }
}

@Composable
fun MyDealsShow(state: MainScreenState.MyDealsState, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(paddingValues)
    ) {
        items(
            items = state.listOfDealDomain,
            key = { it.id }
        ) {
            DealCard(dealDomain = it)
        }
    }
}
