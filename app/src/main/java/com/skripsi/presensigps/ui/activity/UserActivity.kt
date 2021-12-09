package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.UserViewPagerFragment

class UserActivity : AppCompatActivity() {
    val back: ImageView by lazy { findViewById(R.id.icBack) }
    val addUser: FloatingActionButton by lazy { findViewById(R.id.fabAddUser) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        loadFragment(UserViewPagerFragment())

        back.setOnClickListener { finish() }

        addUser.setOnClickListener {
            startActivity(Intent(applicationContext, AddUserActivity::class.java))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameUser, fragment)
            commit()
        }
    }
}