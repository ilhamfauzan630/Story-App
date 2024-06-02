package com.zanacademy.myfirstsubmissionintermediate.view.addstory

import androidx.lifecycle.ViewModel
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository
import java.io.File

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadImage(token: String, file: File, description: String) = repository.uploadImage(token, file, description)
}