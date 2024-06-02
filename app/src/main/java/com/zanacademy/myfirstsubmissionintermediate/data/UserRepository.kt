package com.zanacademy.myfirstsubmissionintermediate.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.zanacademy.myfirstsubmissionintermediate.data.api.ApiService
import com.zanacademy.myfirstsubmissionintermediate.data.entity.StoryItemDatabase
import com.zanacademy.myfirstsubmissionintermediate.data.response.LoginResponse
import com.zanacademy.myfirstsubmissionintermediate.data.response.RegisterResponse
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserModel
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserPreference
import com.zanacademy.myfirstsubmissionintermediate.data.response.FileUploadResponse
import com.zanacademy.myfirstsubmissionintermediate.data.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val storyItemDatabase: StoryItemDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun userLogin(email: String, password: String) = liveData  {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun userRegister(name: String, email: String, password: String) = liveData {
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyItemDatabase, apiService, token),
            pagingSourceFactory = {
//                StoryPagingSource(apiService, token)
                storyItemDatabase.storyItemDao().getAllStories()
            }
        ).liveData
    }

    fun getStoriesWithLocation(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesWithLocation(token)
            emit(ResultState.Success(successResponse))
        } catch (e: Exception) {
            Log.d("ListStory", "getStory: ${e.message.toString()} ")
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun uploadImage(token: String, imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(token, multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            storyItemDatabase: StoryItemDatabase,
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(storyItemDatabase ,apiService,userPreference)
            }.also { instance = it }
    }
}