package com.example.ntpropatsaev.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DealDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun ntProDao(): NtProDao

    companion object {

        private var db: AppDataBase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDataBase {
            db?.let { return it }
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = AppDataBase::class.java,
                    name = DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                db = instance
                return instance
            }
        }
    }
}