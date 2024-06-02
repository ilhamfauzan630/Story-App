package com.zanacademy.myfirstsubmissionintermediate.view.maps

import androidx.lifecycle.ViewModel
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository

class MapsViewModel(private val repository: UserRepository): ViewModel() {
    fun getStoryWithLocation(token: String) = repository.getStoriesWithLocation(token)
}