package com.nagwa.files.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nagwa.files.R
import com.nagwa.files.presentation.home.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        handler = Handler(mainLooper)
        runnable = Runnable {
            MainActivity().start(this)
        }
        handler.postDelayed(runnable, 2000)
    }

    /**
     * Handle if for any case user closes the app while it is starting so clear the handlers
     */
    override fun onPause() {
        super.onPause()
        if (handler != null)
            handler.removeCallbacks(runnable)
    }
}