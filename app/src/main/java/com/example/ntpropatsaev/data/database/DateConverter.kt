package com.example.ntpropatsaev.data.database

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun dateToLong(date: Date) = date.time

    @TypeConverter
    fun longToDate(value: Long) = Date(value)
}