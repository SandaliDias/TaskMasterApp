package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // Splash screen timeout in milliseconds (3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Create a Handler to delay the start of the MainActivity
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            // Start the MainActivity after the splash timeout
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close the splash activity
        }, SPLASH_TIME_OUT)
    }
}