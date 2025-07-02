package com.project.animalface_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.animalface_app.repository.LoginRepository
import com.project.animalface_app.dto.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    fun login(memberId: String, memberPw: String) {
        viewModelScope.launch {
            try {
                val response = loginRepository.login(memberId, memberPw)
                _loginResult.value = response
                if (response != null) {
                    Log.d("LoginViewModel", "로그인 성공")
                } else {
                    Log.e("LoginViewModel", "로그인 실패")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "로그인 오류", e)
                _loginResult.value = null
            }
        }
    }

    fun deleteUser(memberId: String, token: String) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = loginRepository.deleteUser("Bearer $token", memberId)
                _deleteResult.value = response.isSuccessful
                if (response.isSuccessful) {
                    Log.d("LoginViewModel", "회원 삭제 성공")
                } else {
                    Log.e("LoginViewModel", "회원 삭제 실패: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "회원 삭제 오류", e)
                _deleteResult.value = false
            }
        }
    }
}
