package com.project.animalface_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.animalface_app.createGameAPI.CreateGameMainActivity

class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        Log.d("Main2Activity", "Activity 생성됨")

        val createGame: Button = findViewById(R.id.createGame)
        createGame.setOnClickListener {
            val intent = Intent(this, CreateGameMainActivity::class.java)
            startActivity(intent)
            Log.d("Main2Activity", "게임 생성 클릭")
        }

    }
}
