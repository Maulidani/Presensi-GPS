package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.skripsi.presensigps.R

class AdminManagerMainActivity : AppCompatActivity() {
    private val presenceMenu: ConstraintLayout by lazy { findViewById(R.id.parentPresence) }
    private val reportMenu: ConstraintLayout by lazy { findViewById(R.id.parentReport) }
    private val userMenu: ConstraintLayout by lazy { findViewById(R.id.parentUser) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manager_main)

        presenceMenu.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PresenceActivity::class.java
                )
            )
        }
        reportMenu.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ReportActivity::class.java
                )
            )
        }
        userMenu.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UserActivity::class.java
                )
            )
        }
    }
}