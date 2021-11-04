package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.PresenceViewPagerFragment
import com.skripsi.presensigps.ui.fragment.ReportViewPagerFragment

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        loadFragment(ReportViewPagerFragment())

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameReport, fragment)
            commit()
        }
    }
}