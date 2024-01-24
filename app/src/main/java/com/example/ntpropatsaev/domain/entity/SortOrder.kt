package com.example.ntpropatsaev.domain.entity

sealed class SortOrder(val name: String) {

    data object DataChange: SortOrder("Дата изменения сделки")
    data object InstrumentName: SortOrder("Имя инструмента")
    data object PriceOfDeal: SortOrder("Цена сделки")
    data object AmountOfDeal: SortOrder("Объем сделки")
    data object SideOfDeal: SortOrder("Сторона сделки")
}