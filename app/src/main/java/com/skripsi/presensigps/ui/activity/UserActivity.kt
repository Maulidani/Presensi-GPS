package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.ui.fragment.UserViewPagerFragment
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper

class UserActivity : AppCompatActivity() {
    val back: ImageView by lazy { findViewById(R.id.icBack) }
    val addUser: FloatingActionButton by lazy { findViewById(R.id.fabAddUser) }

    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        loadFragment(UserViewPagerFragment())
        sharedPref = PreferencesHelper(this)
        val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

        back.setOnClickListener { finish() }

        if (userPosition == "admin") {
            addUser.setOnClickListener {
                startActivity(Intent(applicationContext, AddUserActivity::class.java))
            }
        } else {
            addUser.visibility = View.GONE
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameUser, fragment)
            commit()
        }
    }
}