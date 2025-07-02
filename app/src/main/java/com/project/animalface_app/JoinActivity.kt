package com.project.animalface_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.animalface_app.databinding.ActivityJoinBinding
import com.project.animalface_app.dto.UserDTO
import com.project.animalface_app.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnComplete.setOnClickListener {
            val memberId = binding.userid.text.toString()
            val memberName = binding.nickname.text.toString()
            val memberPw = binding.password.text.toString()
            val memberPw2 = binding.password2.text.toString()

            if (memberPw != memberPw2) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (memberId.isEmpty() || memberName.isEmpty() || memberPw.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userDTO = UserDTO(memberId = memberId, memberName = memberName, memberPw = memberPw)

            val networkService = (applicationContext as MyApplication).networkService
            val call = networkService.registerUser(userDTO)
            call.enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@JoinActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@JoinActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@JoinActivity, "회원가입 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(this@JoinActivity, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}