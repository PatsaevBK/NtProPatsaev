package com.example.ntpropatsaev.di

import android.content.Context
import com.example.ntpropatsaev.data.database.AppDataBase
import com.example.ntpropatsaev.data.database.NtProDao
import com.example.ntpropatsaev.data.repository.RepositoryImpl
import com.example.ntpropatsaev.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @ApplicationScope
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    companion object {

        @ApplicationScope
        @Provides
        fun provideNtProDao(
            context: Context
        ): NtProDao {
            return AppDataBase.getInstance(context).ntProDao()
        }

    }
}