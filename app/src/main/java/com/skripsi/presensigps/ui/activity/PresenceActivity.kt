package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.PresenceFragment
import com.skripsi.presensigps.ui.fragment.PresenceViewPagerFragment

class PresenceActivity : AppCompatActivity() {
    val back: ImageView by lazy { findViewById(R.id.icBack) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)
        loadFragment(PresenceFragment("all"))

        back.setOnClickListener { finish() }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.framePresence, fragment)
            commit()
        }
    }
}