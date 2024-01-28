package com.example.ntpropatsaev.data.repository

import android.app.Application
import com.example.ntpropatsaev.data.database.AppDataBase
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                SortType.DATA_CHANGE -> appDao.getAllSortedByDate(it.second.isAsc)
                SortType.INSTRUMENT_NAME -> appDao.getAllSortedByInstrumentName(it.second.isAsc)
                SortType.PRICE_OF_DEAL -> appDao.getAllSortedByPrice(it.second.isAsc)
                SortType.AMOUNT_OF_DEAL -> appDao.getAllSortedByAmount(it.second.isAsc)
                SortType.SIDE_OF_DEAL -> appDao.getAllSortedBySide(it.second.isAsc)
            }
        }
        .map { it.mapDealDbModelToDealDomain() }
        .map {
            DealsResult.Success(
                listOfDeals = it,
                sortType = currentSortType,
                sortOrder = currentSortOrder
            )
        }

    private val loadedDeals = flow {
        nextDataComeEvent.emit(Unit)
        nextDataComeEvent.collect {
            emit(listOfDeal)
        }
    }.map { DealsResult.Success(it, currentSortType, currentSortOrder) as DealsResult }
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            DealsResult.Success(listOfDeal, currentSortType, currentSortOrder)
        )

    private val nextDataComeEvent = MutableSharedFlow<Unit>(replay = 1)

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

    private suspend fun refreshListInFlow() {
        val list = changeSortPattern(newMyDealList, currentSortType, currentSortOrder)
        mutableListOfDeal.clear()
//        mutableListOfDeal.addAll(list.map { it.mapDealToDomainDeal() })
        nextDataComeEvent.emit(Unit)
    }

    override suspend fun changeSortType(sortType: SortType) {
        currentSortType = sortType
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
//        sortOrderRep = sortOrder
//        refreshListInFlow()
    }

    override suspend fun changeSorOrder(sortOrder: SortOrder) {
        currentSortOrder = sortOrder
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
//        sortTypeRep = sortType
//        refreshListInFlow()
    }

    private fun changeSortPattern(
        list: List<Deal>,
        sortType: SortType,
        sortOrder: SortOrder
    ): List<Deal> {
        val comparator = when (sortType) {
            SortType.DATA_CHANGE -> Comparator { t1: Deal, t2: Deal ->
                t1.timeStamp.compareTo(t2.timeStamp)
            }

            SortType.INSTRUMENT_NAME -> Comparator { t1: Deal, t2: Deal ->
                t1.instrumentName.compareTo(t2.instrumentName)
            }

            SortType.PRICE_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.price.compareTo(t2.price)
            }

            SortType.AMOUNT_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.amount.compareTo(t2.amount)
            }

            SortType.SIDE_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.side.compareTo(t2.side)
            }
        }
        return when (sortOrder) {
            SortOrder.ASC -> {
                list.sortedWith(comparator)
            }

            SortOrder.DESC -> {
                list.sortedWith(comparator).reversed()
            }
        }
    }
}