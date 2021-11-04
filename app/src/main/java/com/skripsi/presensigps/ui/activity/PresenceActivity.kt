package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.PresenceViewPagerFragment

class PresenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)
        loadFragment(PresenceViewPagerFragment())

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.framePresence, fragment)
            commit()
        }
    }
}