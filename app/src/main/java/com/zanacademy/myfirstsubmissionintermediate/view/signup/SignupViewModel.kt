package com.zanacademy.myfirstsubmissionintermediate.view.signup

import androidx.lifecycle.ViewModel
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    fun userRegister(name: String, email: String, password: String) = repository.userRegister(name, email, password)
}