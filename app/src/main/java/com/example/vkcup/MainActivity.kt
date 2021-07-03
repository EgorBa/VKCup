package com.example.vkcup

import com.example.vkcup.news.MainActivityNews
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.vkcup.crosszero.CrossZero
import com.example.vkcup.taxi.Taxi
import com.example.vkcup.voice.Voice

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val taxi : Button = findViewById(R.id.taxi)
        taxi.setOnClickListener{
            val intent = Intent(this@MainActivity, Taxi::class.java)
            startActivity(intent)
        }

        val crossZero : Button = findViewById(R.id.crosszero)
        crossZero.setOnClickListener{
            val intent = Intent(this@MainActivity, CrossZero::class.java)
            startActivity(intent)
        }

        val news : Button = findViewById(R.id.news)
        news.setOnClickListener{
            val intent = Intent(this@MainActivity, MainActivityNews::class.java)
            startActivity(intent)
        }

        val voice : Button = findViewById(R.id.voice)
        voice.setOnClickListener{
            val intent = Intent(this@MainActivity, Voice::class.java)
            startActivity(intent)
        }

        val video : Button = findViewById(R.id.video)
        video.setOnClickListener{
            Toast.makeText(applicationContext, "Добавим в следующей версии)", Toast.LENGTH_LONG)
                .show()
        }
    }
}