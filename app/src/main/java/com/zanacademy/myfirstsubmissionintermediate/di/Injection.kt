package com.zanacademy.myfirstsubmissionintermediate.di

import android.content.Context
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository
import com.zanacademy.myfirstsubmissionintermediate.data.api.ApiConfig
import com.zanacademy.myfirstsubmissionintermediate.data.entity.StoryItemDatabase
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserPreference
import com.zanacademy.myfirstsubmissionintermediate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = StoryItemDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(database, apiService, pref)
    }
}