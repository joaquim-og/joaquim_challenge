package com.joaquim.joaquim_teste.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.SharedPrefs
import com.joaquim.joaquim_teste.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        setContentView(binding.root)
    }

    override fun onDestroy() {
        SharedPrefs.clearSharedPreferences()
        super.onDestroy()
    }
}