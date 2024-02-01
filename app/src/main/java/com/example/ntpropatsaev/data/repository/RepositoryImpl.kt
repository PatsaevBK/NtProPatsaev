package com.example.ntpropatsaev.data.repository

import com.example.ntpropatsaev.data.database.NtProDao
import com.example.ntpropatsaev.data.mapper.mapDealDbModelToDeal
import com.example.ntpropatsaev.data.mapper.mapDealDtoToDealDbModel
import com.example.ntpropatsaev.data.server.Server
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.SortType
import com.example.ntpropatsaev.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImpl @Inject constructor(
    private val server: Server,
    private val appDao: NtProDao
) : Repository {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var dealsSubscription: Job? = null

    private var currentSortType = SortType.DATE_CHANGE

    private var currentSortOrder = SortOrder.DESC

    private val optionOfSortFlow = MutableStateFlow(Pair(currentSortType, currentSortOrder))
    private val sortedListDeals = optionOfSortFlow
        .flatMapLatest {
            when (it.first) {
                SortType.DATE_CHANGE -> appDao.getAllSortedByDate(it.second.isAsc)
                SortType.INSTRUMENT_NAME -> appDao.getAllSortedByInstrumentName(it.second.isAsc)
                SortType.PRICE_OF_DEAL -> appDao.getAllSortedByPrice(it.second.isAsc)
                SortType.AMOUNT_OF_DEAL -> appDao.getAllSortedByAmount(it.second.isAsc)
                SortType.SIDE_OF_DEAL -> appDao.getAllSortedBySide(it.second.isAsc)
            }
        }
        .map { it.mapDealDbModelToDeal() }
        .map {
            DealsResult(
                listDealDbModel = it,
                sortType = currentSortType,
                sortOrder = currentSortOrder
            )
        }

    override fun getDeals(): Flow<DealsResult> {
        return sortedListDeals
    }

    override fun loadDeals() {
        dealsSubscription = subscribeToDeals()
            .onEach { dealsDto ->
                delay(1000)
                appDao.insertDealsList(dealsDto.mapDealDtoToDealDbModel())
            }
            .launchIn(coroutineScope)
    }

    private fun subscribeToDeals(): Flow<List<Server.DealDto>> = callbackFlow {
        server.subscribeToDeals {
            trySend(it)
        }
        awaitClose()
    }

    override suspend fun changeSortType(sortType: SortType) {
        currentSortType = sortType
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
    }

    override suspend fun changeSortOrder(sortOrder: SortOrder) {
        currentSortOrder = sortOrder
        optionOfSortFlow.emit(
            Pair(currentSortType, currentSortOrder)
        )
    }

    override suspend fun clearDb() {
        appDao.clearDealDbModel()
    }
}