package com.example.ntpropatsaev.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.usecases.ChangeSortOrderUseCase
import com.example.ntpropatsaev.domain.usecases.ChangeSortTypeUseCase
import com.example.ntpropatsaev.domain.usecases.GetDealsUseCase
import com.example.ntpropatsaev.domain.usecases.LoadNewDealsUseCase
import com.example.ntpropatsaev.extentions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getDealsUseCase: GetDealsUseCase,
    private val loadNewDealsUseCase: LoadNewDealsUseCase,
    private val changeSortOrderUseCase: ChangeSortOrderUseCase,
    private val changeSortTypeUseCase: ChangeSortTypeUseCase
) : ViewModel() {

    private val loadingStateEvent = MutableSharedFlow<Unit>()
    private val loadingState = flow<MainScreenState> {
        loadingStateEvent.collect {
            emit(MainScreenState.Loading)
        }
    }

    val screenState = getDealsUseCase()
        .map { it as DealsResult.Success }
        .onEach { Log.d("MainViewModel", "Paint list of ${it.listDealDbModel.size}") }
        .map {
            MainScreenState.Success(
                it.listDealDbModel,
                it.sortType,
                it.sortOrder
            ) as MainScreenState
        }
        .mergeWith(loadingState)


    init {
        loadNewDealsUseCase()
    }

    fun onSortOrderClick(sortType: SortType) {
        viewModelScope.launch {
            loadingStateEvent.emit(Unit)
            changeSortOrderUseCase(sortType)
        }
    }

    fun onChangeTypeClick(sortOrder: SortOrder) {
        viewModelScope.launch {
            changeSortTypeUseCase(sortOrder)
        }
    }
}