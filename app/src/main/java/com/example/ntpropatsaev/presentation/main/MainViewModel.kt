package com.example.ntpropatsaev.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ntpropatsaev.data.repository.RepositoryImpl
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.usecases.ChangeSortOrderUseCase
import com.example.ntpropatsaev.domain.usecases.ChangeUpDownUseCase
import com.example.ntpropatsaev.domain.usecases.GetDealsUseCase
import com.example.ntpropatsaev.domain.usecases.LoadNewDealsUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)
    private val getDealsUseCase = GetDealsUseCase(repository)
    private val loadNewDealsUseCase = LoadNewDealsUseCase(repository)
    private val changeSortOrderUseCase = ChangeSortOrderUseCase(repository)
    private val changeUpDownUseCase = ChangeUpDownUseCase(repository)

    val screenState = getDealsUseCase()
        .map { it as DealsResult.Success }
        .filter { it.listOfDeals.isNotEmpty() }
        .onEach { Log.d("MainViewModel", "size of list = ${it.listOfDeals.size}") }
        .map {
            MainScreenState.MyDealsState(
                it.listOfDeals,
                it.sortType,
                it.sortOrder
            ) as MainScreenState
        }
        .onStart { emit(MainScreenState.Loading) }


    init {
        loadDeals()
    }

    private fun loadDeals() {
        loadNewDealsUseCase()
    }

    fun onSortOrderClick(sortType: SortType) {
        viewModelScope.launch {
            changeSortOrderUseCase(sortType)
        }
    }

    fun onUpDownClick(sortOrder: SortOrder) {
        viewModelScope.launch {
            changeUpDownUseCase(sortOrder)
        }
    }

}