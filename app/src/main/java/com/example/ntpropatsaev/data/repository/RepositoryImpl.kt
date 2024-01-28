package com.example.ntpropatsaev.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.ntpropatsaev.data.database.AppDataBase
import com.example.ntpropatsaev.data.database.DealDbModel
import com.example.ntpropatsaev.data.mapper.mapDealDbModelToDealDomain
import com.example.ntpropatsaev.data.mapper.mapDealToDealDbModel
import com.example.ntpropatsaev.data.server.Server
import com.example.ntpropatsaev.data.server.Server.Deal
import com.example.ntpropatsaev.domain.entity.DealDomain
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImpl(
    private val application: Application
) : Repository {

    private val server = Server()
    private val appDao = AppDataBase.getInstance(application).ntProDao()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val mutableListOfDeal = mutableListOf<DealDomain>()
    private val listOfDeal: List<DealDomain>
        get() = mutableListOfDeal.toList()

    private var newMyDealList = listOf<Deal>()


    private var currentSortType = SortType.DATA_CHANGE

    private var currentSortOrder = SortOrder.DESC

    private val optionOfSortFlow = MutableStateFlow(Pair(currentSortType, currentSortOrder))
    private val sortedListDeals = optionOfSortFlow
        .flatMapLatest {
            when (it.first) {
                SortType.DATA_CHANGE -> {
                    Pager(
                        config = PagingConfig(20),
                    ) {
                        appDao.getAllSortedByDate(it.second.isAsc)
                    }.flow
                }

                SortType.INSTRUMENT_NAME -> TODO()
                SortType.PRICE_OF_DEAL -> TODO()
                SortType.AMOUNT_OF_DEAL -> TODO()
                SortType.SIDE_OF_DEAL -> TODO()
            }
        }
        .map { value: PagingData<DealDbModel> ->
            value.map {
                it.mapDealDbModelToDealDomain()
            }
        }
        .map {
            DealsResult.Success(
                it,
                currentSortType,
                currentSortOrder
            )
        }


    override fun getDeals(): Flow<DealsResult> {
        return sortedListDeals
    }

    override fun loadDeals() {
        server.subscribeToDeals { dealList ->
            val job = coroutineScope.launch {
                val listDealDbModel = dealList.mapDealToDealDbModel()
                appDao.insertDealsList(listDealDbModel)
            }
        }
    }

    override suspend fun changeSortType(sortType: SortType) {
        currentSortType = sortType
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
    }

    override suspend fun changeSorOrder(sortOrder: SortOrder) {
        currentSortOrder = sortOrder
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
    }
}