package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.PresenceViewPagerFragment
import com.skripsi.presensigps.ui.fragment.UserViewPagerFragment

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        loadFragment(UserViewPagerFragment())

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameUser, fragment)
            commit()
        }
    }
}