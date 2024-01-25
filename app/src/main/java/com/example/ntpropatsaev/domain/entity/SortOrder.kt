package com.example.ntpropatsaev.domain.entity

enum class SortOrder(val nameOfOrder: String) {
    DATA_CHANGE("Дата изменения сделки"),
    INSTRUMENT_NAME("Имя инструмента"),
    PRICE_OF_DEAL("Цена сделки"),
    AMOUNT_OF_DEAL("Объем сделки"),
    SIDE_OF_DEAL("Сторона сделки")
}