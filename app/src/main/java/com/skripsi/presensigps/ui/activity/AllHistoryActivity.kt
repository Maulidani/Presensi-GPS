package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.AllHistoryViewPagerFragment

class AllHistoryActivity : AppCompatActivity() {
    val back: ImageView by lazy { findViewById(R.id.icBack) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_history)
        loadFragment(AllHistoryViewPagerFragment())

        back.setOnClickListener { finish() }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameHistory, fragment)
            commit()
        }
    }
}