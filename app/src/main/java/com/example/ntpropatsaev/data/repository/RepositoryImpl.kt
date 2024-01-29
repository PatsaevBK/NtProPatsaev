package com.example.ntpropatsaev.data.repository

import android.app.Application
import android.util.Log
import com.example.ntpropatsaev.data.database.AppDataBase
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.LinkedList

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImpl(
    private val application: Application
) : Repository {

    private val server = Server()
    private val appDao = AppDataBase.getInstance(application).ntProDao()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var dealsSubscription: Job? = null

    private var currentSortType = SortType.DATA_CHANGE

    private var currentSortOrder = SortOrder.DESC

    private val cashedList = LinkedList<List<Server.DealDto>>()

    private val readyToSaveInDbEvent = MutableSharedFlow<Unit>()

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
        .map { it.mapDealDbModelToDeal() }
        .map {
            DealsResult.Success(
                listDealDbModel = it,
                sortType = currentSortType,
                sortOrder = currentSortOrder
            )
        }

    init {
        coroutineScope.launch {
            readyToSaveInDbEvent.collect {
                savePackageToDb()
            }
        }
    }

    override fun getDeals(): Flow<DealsResult> {
        return sortedListDeals
    }

    override fun loadDeals() {
        dealsSubscription = subscribeToDeals()
            .onEach { dealDtoList ->
                Log.d("RepositoryImpl", "Cashed new List ${dealDtoList.size}")
                cashedList.add(dealDtoList)
            }
            .onStart {
                readyToSaveInDbEvent.emit(Unit)
            }
            .launchIn(coroutineScope)
    }

    private fun subscribeToDeals(): Flow<List<Server.DealDto>> = callbackFlow {
        server.subscribeToDeals {
            Log.d("RepositoryImpl", "Server sends new List ${it.size}")
            trySend(it)
        }
        awaitClose()
    }.buffer(1000)

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

    private suspend fun savePackageToDb() {
        do {
            val listReadyToWrite = mutableListOf<Server.DealDto>()
            repeat(10) {
                cashedList.poll()?.let {
                    listReadyToWrite.addAll(it)
                }
            }
            delay(1000)
            Log.d("RepositoryImpl", "Write new List in db ${listReadyToWrite.size}")
            appDao.insertDealsList(listReadyToWrite.mapDealDtoToDealDbModel())
        } while (cashedList.size > 0)
    }
}