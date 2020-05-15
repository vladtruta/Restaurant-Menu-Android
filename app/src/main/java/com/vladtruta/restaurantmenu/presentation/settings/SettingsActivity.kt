package com.vladtruta.restaurantmenu.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladtruta.restaurantmenu.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(findViewById(R.id.settings_mtb))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
