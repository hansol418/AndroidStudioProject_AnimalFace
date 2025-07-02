package com.project.animalface_app.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.animalface_app.repository.LoginRepository
import com.project.animalface_app.viewModel.LoginViewModel

class LoginViewModelFactory(
    private val loginRepository: LoginRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}