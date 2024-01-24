package com.example.ntpropatsaev.data.repository

import com.example.ntpropatsaev.data.Server
import com.example.ntpropatsaev.data.Server.Deal
import com.example.ntpropatsaev.data.mapper.Mapper
import com.example.ntpropatsaev.domain.entity.DealsResult
import com.example.ntpropatsaev.domain.entity.MyDeal
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

    private var newMyDealList = listOf<Deal>()

    private val loadedDeals = flow {
        nextDataComeEvent.emit(Unit)
        nextDataComeEvent.collect {
            emit(listOfDeal)
        }
    }.map { DealsResult.Success(it) as DealsResult }

        .stateIn(
            coroutineScope,
            SharingStarted.Lazily,
            DealsResult.Success(listOfDeal)
        )

    private val nextDataComeEvent = MutableSharedFlow<Unit>(replay = 1)
    private val listOfDeal: List<MyDeal>
        get() = mutableListOfDeal.toList()

    override fun getDeals(): StateFlow<DealsResult> {
        return loadedDeals
    }

    override fun newDealsCome() {

        server.subscribeToDeals { dealList ->
            newMyDealList = dealList
        }
        coroutineScope.launch {
            while (true) {
                mutableListOfDeal.clear()
                mutableListOfDeal.addAll(newMyDealList.map { mapper.mapDealToMyDeal(it) })
                nextDataComeEvent.emit(Unit)
                delay(1000)
            }
        }
    }

}