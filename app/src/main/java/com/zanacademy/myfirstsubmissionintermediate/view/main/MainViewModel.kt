package com.zanacademy.myfirstsubmissionintermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserModel
import com.zanacademy.myfirstsubmissionintermediate.data.response.ListStoryItem
import com.zanacademy.myfirstsubmissionintermediate.data.response.StoryResponse
import kotlinx.coroutines.launch


class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        repository.getStories(token).cachedIn(viewModelScope)
}
