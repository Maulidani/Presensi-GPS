package com.skripsi.presensigps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.skripsi.presensigps.ui.activity.AdminManagerMainActivity
import com.skripsi.presensigps.ui.activity.LoginActivity
import com.skripsi.presensigps.ui.activity.MainActivity
import com.skripsi.presensigps.ui.activity.SalesMainActivity
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPref = PreferencesHelper(this)
        val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)

            if (sharedPref.getBoolean(Constant.PREF_IS_LOGIN)) {
                Log.e(this.toString(), "Anda sudah login")
                if (userPosition == "admin" || userPosition == "manager") {
                    startActivity(
                        Intent(
                            this@SplashScreenActivity,
                            AdminManagerMainActivity::class.java
                        )
                    )
                } else {
                    startActivity(Intent(this@SplashScreenActivity, SalesMainActivity::class.java))
                }
            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}