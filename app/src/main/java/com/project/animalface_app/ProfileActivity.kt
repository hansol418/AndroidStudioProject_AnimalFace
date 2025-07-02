package com.project.animalface_app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.project.animalface_app.databinding.ActivityProfileBinding
import com.project.animalface_app.repository.LoginRepository
import com.project.animalface_app.retrofit.INetworkService
import com.project.animalface_app.retrofit.MyApplication
import com.project.animalface_app.viewModel.LoginViewModel
import com.project.animalface_app.viewModelFactory.LoginViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var networkService: INetworkService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val myApplication = application as MyApplication
        networkService = myApplication.getApiService()
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val repository = LoginRepository(networkService, sharedPreferences)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(repository)).get(
            LoginViewModel::class.java
        )


        findViewById<ImageButton>(R.id.btn_delete_account).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("계정 삭제")
                .setMessage("정말로 계정을 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, which ->
                    val memberId = getMemberId()
                    val token = getJwtToken()

                    if (memberId != null && token != null) {
                        loginViewModel.deleteUser(memberId, token)
                    } else {
                        Toast.makeText(this, "회원 정보나 토큰을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소", null)
                .show()
        }


        loginViewModel.deleteResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "회원 삭제 성공", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "회원 삭제 실패", Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getMemberId(): String? {
        val memberId = sharedPreferences.getString("memberId", "123")
        return if (memberId != null) {
            Log.d("ProfileActivity", "회원 번호 로드됨: $memberId")
            memberId
        } else {
            Log.e("ProfileActivity", "회원 번호가 SharedPreferences에 없습니다.")
            null
        }
    }

    private fun getJwtToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }
}
