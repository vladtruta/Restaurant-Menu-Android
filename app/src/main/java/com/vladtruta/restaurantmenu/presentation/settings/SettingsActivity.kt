package com.vladtruta.restaurantmenu.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladtruta.restaurantmenu.R
import com.vladtruta.restaurantmenu.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_settings)

//        setSupportActionBar(binding.settingsMtb)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
