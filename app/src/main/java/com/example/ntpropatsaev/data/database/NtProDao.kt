package com.example.ntpropatsaev.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NtProDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealsList(listDealDbModel: List<DealDbModel>)

    @Query(
        "SELECT * FROM full_deals_list ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN date END ASC, " +
                "CASE WHEN :isAsc = 2 THEN date END DESC"
    )
    fun getAllSortedByDate(isAsc: Int): Flow<List<DealDbModel>>

    @Query(
        "SELECT * FROM full_deals_list ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN instrumentName END ASC, " +
                "CASE WHEN :isAsc = 2 THEN instrumentName END DESC"
    )
    fun getAllSortedByInstrumentName(isAsc: Int): Flow<List<DealDbModel>>

    @Query(
        "SELECT * FROM full_deals_list ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN price END ASC, " +
                "CASE WHEN :isAsc = 2 THEN price END DESC"
    )
    fun getAllSortedByPrice(isAsc: Int): Flow<List<DealDbModel>>

    @Query(
        "SELECT * FROM full_deals_list ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN amount END ASC, " +
                "CASE WHEN :isAsc = 2 THEN amount END DESC"
    )
    fun getAllSortedByAmount(isAsc: Int): Flow<List<DealDbModel>>

    @Query(
        "SELECT * FROM full_deals_list ORDER BY " +
                "CASE WHEN :isAsc = 1 THEN side END ASC, " +
                "CASE WHEN :isAsc = 2 THEN side END DESC"
    )
    fun getAllSortedBySide(isAsc: Int): Flow<List<DealDbModel>>

    @Query("DELETE FROM full_deals_list")
    suspend fun clearDealDbModel()
}