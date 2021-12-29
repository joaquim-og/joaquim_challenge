package com.joaquim.joaquim_teste.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.joaquim.joaquim_teste.R
import com.joaquim.joaquim_teste.data.commom.SharedPrefs
import com.joaquim.joaquim_teste.databinding.ActivityHomeBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeEventViewModel by viewModel()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )

        homeViewModel.createLocalUser()
        homeViewModel.searchEventsData()

        setContentView(binding.root)
    }

    override fun onDestroy() {
        SharedPrefs.clearSharedPreferences()
        super.onDestroy()
    }
}