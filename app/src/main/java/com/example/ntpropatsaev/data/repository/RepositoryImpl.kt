package com.example.ntpropatsaev.data.repository

import com.example.ntpropatsaev.data.Server
import com.example.ntpropatsaev.data.Server.Deal
import com.example.ntpropatsaev.data.mapper.Mapper
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.MyDeal
import com.example.ntpropatsaev.domain.entity.SortOrder
import com.example.ntpropatsaev.domain.entity.UpDown
import com.example.ntpropatsaev.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RepositoryImpl : Repository {

    private val server = Server()

    private val mapper = Mapper()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val mutableListOfDeal = mutableListOf<MyDeal>()
    private val listOfDeal: List<MyDeal>
        get() = mutableListOfDeal.toList()

    private var newMyDealList = listOf<Deal>()


    private var sortOrderRep = SortOrder.DATA_CHANGE

    private var upDownRep = UpDown.DOWN

    private val loadedDeals = flow {
        nextDataComeEvent.emit(Unit)
        nextDataComeEvent.collect {
            emit(listOfDeal)
        }
    }.map { DealsResult.Success(it, sortOrderRep, upDownRep) as DealsResult }
        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            DealsResult.Success(listOfDeal, sortOrderRep, upDownRep)
        )

    private val nextDataComeEvent = MutableSharedFlow<Unit>(replay = 1)

    override fun getDeals(): StateFlow<DealsResult> {
        return loadedDeals
    }

    override fun loadDeals() {
        server.subscribeToDeals { dealList ->
            newMyDealList = dealList
        }
        coroutineScope.launch {
            while (true) {
                refreshListInFlow()
                delay(1000)
            }
        }
    }

    private suspend fun refreshListInFlow() {
        val list = changeSortPattern(newMyDealList, sortOrderRep, upDownRep)
        mutableListOfDeal.clear()
        mutableListOfDeal.addAll(list.map { mapper.mapDealToMyDeal(it) })
        nextDataComeEvent.emit(Unit)
    }

    override suspend fun changeSortOrder(sortOrder: SortOrder) {
        sortOrderRep = sortOrder
        refreshListInFlow()
    }

    override suspend fun changeUpDown(upDown: UpDown) {
        upDownRep = upDown
        refreshListInFlow()
    }

    private fun changeSortPattern(list: List<Deal>, sortOrder: SortOrder, upDown: UpDown): List<Deal> {
        val comparator = when (sortOrder) {
            SortOrder.DATA_CHANGE -> Comparator { t1: Deal, t2: Deal ->
                t1.timeStamp.compareTo(t2.timeStamp)
            }
            SortOrder.INSTRUMENT_NAME -> Comparator { t1: Deal, t2: Deal ->
                t1.instrumentName.compareTo(t2.instrumentName)
            }
            SortOrder.PRICE_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.price.compareTo(t2.price)
            }
            SortOrder.AMOUNT_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.amount.compareTo(t2.amount)
            }
            SortOrder.SIDE_OF_DEAL -> Comparator { t1: Deal, t2: Deal ->
                t1.side.compareTo(t2.side)
            }
        }
        return when (upDown) {
            UpDown.UP -> {
                list.sortedWith(comparator)
            }
            UpDown.DOWN -> {
                list.sortedWith(comparator).reversed()
            }
        }
    }
}