package com.example.ntpropatsaev.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ntpropatsaev.data.repository.RepositoryImpl
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown
import com.example.ntpropatsaev.domain.usecases.GetDealsUseCase
import com.example.ntpropatsaev.domain.usecases.NewDealsComeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class MainViewModel(): ViewModel() {

    private val repository = RepositoryImpl()
    private val getDealsUseCase = GetDealsUseCase(repository)
    private val newDealsComeUseCase = NewDealsComeUseCase(repository)

    private val dealsResult = getDealsUseCase()

    val screenState: Flow<MainScreenState> = dealsResult
        .map { it as DealsResult.Success }
        .filter { it.listOfDeals.isNotEmpty() }
        .map { MainScreenState.MyDealsState(it.listOfDeals) as MainScreenState }
        .onStart { emit(MainScreenState.Initial) }
        .onEach { Log.d("MainViewModel", it.toString()) }

    private val _sortOrder = MutableStateFlow(SortOrder.DataChange)
    val sortOrder: StateFlow<SortOrder>
        get() = _sortOrder.asStateFlow()

    private val _upDown = MutableStateFlow(UpDown.Down)

    fun loadDeals() {
        newDealsComeUseCase()
    }
}