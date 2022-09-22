package com.example.android.unscramble

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Membuat Aktivitas yang menghosting fragmen Game di aplikasi
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}