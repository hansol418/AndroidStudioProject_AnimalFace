package com.project.animalface_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class CreateGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_game)

        val gameName = findViewById<EditText>(R.id.createGameName)
        val question = findViewById<EditText>(R.id.createQuestion)
        val answer = findViewById<EditText>(R.id.createAnswer)
        val result = findViewById<EditText>(R.id.createResult)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        submitButton.setOnClickListener {
            if (validateForm(gameName, question, answer, result)) {
                submitForm(gameName.text.toString(), question.text.toString(), answer.text.toString(), result.text.toString())
            } else {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        resetButton.setOnClickListener {
            gameName.text.clear()
            question.text.clear()
            answer.text.clear()
            result.text.clear()
        }
    }

    private fun validateForm(vararg fields: EditText): Boolean {
        return fields.all { it.text.isNotBlank() }
    }

    private fun submitForm(name: String, question: String, answer: String, result: String) {
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("createGameName", name)
            .add("createQuestion", question)
            .add("createAnswer", answer)
            .add("createResult", result)
            .build()

        val request = Request.Builder()
            .url("https://yourserver.com/createGame/create")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                runOnUiThread {
                    Toast.makeText(this@CreateGameActivity, "서버와의 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateGameActivity, "게임이 성공적으로 생성되었습니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CreateGameActivity, "서버 오류 발생.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
