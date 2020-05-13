package com.vladtruta.restaurantmenu.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vladtruta.restaurantmenu.databinding.ActivitySplashBinding
import com.vladtruta.restaurantmenu.presentation.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        initActions()

        splashViewModel.refresh()
    }

    private fun initObservers() {
        splashViewModel.refreshSuccessful.observe(this, Observer {
            if (it) {
                binding.loadingPb.visibility = View.GONE
                goToHome()
            } else {
                binding.loadingPb.visibility = View.VISIBLE
            }
        })

        splashViewModel.errorMessage.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                binding.tapToRetryTv.visibility = View.GONE
            } else {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.loadingPb.visibility = View.GONE
                binding.tapToRetryTv.visibility = View.VISIBLE
                binding.root.isEnabled = true
            }
        })
    }

    private fun initActions() {
        binding.root.setOnClickListener {
            it.isEnabled = false
            splashViewModel.refresh()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
