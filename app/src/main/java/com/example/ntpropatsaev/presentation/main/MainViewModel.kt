package com.example.ntpropatsaev.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ntpropatsaev.data.repository.RepositoryImpl
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown
import com.example.ntpropatsaev.domain.usecases.ChangeSortOrderUseCase
import com.example.ntpropatsaev.domain.usecases.ChangeUpDownUseCase
import com.example.ntpropatsaev.domain.usecases.GetDealsUseCase
import com.example.ntpropatsaev.domain.usecases.LoadNewDealsUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private val repository = RepositoryImpl()
    private val getDealsUseCase = GetDealsUseCase(repository)
    private val loadNewDealsUseCase = LoadNewDealsUseCase(repository)
    private val changeSortOrderUseCase = ChangeSortOrderUseCase(repository)
    private val changeUpDownUseCase = ChangeUpDownUseCase(repository)

    private val dealsResult = getDealsUseCase()

    val screenState = dealsResult
        .map { it as DealsResult.Success }
        .filter { it.listOfDeals.isNotEmpty() }
        .map {
            MainScreenState.MyDealsState(
                it.listOfDeals,
                it.sortOrder,
                it.upDown
            ) as MainScreenState
        }
        .onStart { emit(MainScreenState.Loading) }

    init {
        loadDeals()
    }

    private fun loadDeals() {
        loadNewDealsUseCase()
    }

    fun onSortOrderClick(sortOrder: SortOrder) {
        viewModelScope.launch {
            changeSortOrderUseCase(sortOrder)
        }
    }

    fun onUpDownClick(upDown: UpDown) {
        viewModelScope.launch {
            changeUpDownUseCase(upDown)
        }
    }

}