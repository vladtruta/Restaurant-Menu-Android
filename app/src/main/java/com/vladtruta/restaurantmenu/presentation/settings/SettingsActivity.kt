package com.vladtruta.restaurantmenu.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladtruta.restaurantmenu.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(settings_mtb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
