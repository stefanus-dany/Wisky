package com.project.wisky

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.wisky.authentication.AuthenticationActivity
import com.project.wisky.databinding.ActivitySplashscreenBinding

class SplashscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ll.alpha = 0f
        binding.logo.alpha = 0f
        binding.ll.animate().setDuration(1500).alpha(1f).withEndAction {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding.logo.animate().setDuration(1500).alpha(1f).withEndAction {
            startActivity(Intent(this, AuthenticationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}