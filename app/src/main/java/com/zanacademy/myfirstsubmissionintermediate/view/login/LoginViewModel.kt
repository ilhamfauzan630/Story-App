package com.zanacademy.myfirstsubmissionintermediate.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zanacademy.myfirstsubmissionintermediate.data.UserRepository
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserModel
import com.zanacademy.myfirstsubmissionintermediate.view.textedit.MyCustomPassword
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun userLogin(email: String, password: String) = repository.userLogin(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}