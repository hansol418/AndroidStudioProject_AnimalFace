package com.project.animalface_app.repository

import android.content.SharedPreferences
import android.util.Log
import com.project.animalface_app.retrofit.INetworkService
import com.project.animalface_app.dto.LoginRequest
import com.project.animalface_app.dto.LoginResponse

import retrofit2.Response

class LoginRepository(private val apiService: INetworkService, private val sharedPreferences: SharedPreferences) {

    suspend fun login(memberId: String, memberPw: String): LoginResponse? {
        val loginRequest = LoginRequest(memberId, memberPw)
        val response = apiService.login(loginRequest)

        if (response.isSuccessful && response.body() != null) {
            val loginResponse = response.body()!!
            val accessToken = loginResponse.accessToken
            val refreshToken = loginResponse.refreshToken
            val responseMemberId = loginResponse.memberId
            Log.d("lsy accessToken","accessToken : ${accessToken}" )
            Log.d("lsy refreshToken","refreshToken : ${refreshToken}" )
            Log.d("lsy responseMemberId","responseMemberId : ${responseMemberId}")

            sharedPreferences.edit().apply {
                putString("jwt_token", accessToken)
                putString("refreshToken", refreshToken)
                putString("memberId", responseMemberId)
                apply()
            }

            val savedMemberId = sharedPreferences.getString("memberId", "123456")
            Log.d("LoginRepository", "저장된 회원 번호: $savedMemberId")
            Log.d("LoginRepository", "응답 회원 번호: $responseMemberId")

            return loginResponse
        } else {
            Log.e("LoginRepository", "API 호출 실패: ${response.code()} ${response.message()}")
            return null
        }
    }

    suspend fun deleteUser(token: String, memberId: String): Response<Unit > {
        return apiService.deleteUser(token, memberId)
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }
}