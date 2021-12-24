package com.joaquim.joaquim_teste.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.SharedPrefs

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onDestroy() {
        SharedPrefs.clearSharedPreferences()
        super.onDestroy()
    }
}