package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.network.UserModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminManagerMainActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val presenceMenu: ConstraintLayout by lazy { findViewById(R.id.parentPresence) }
    private val reportMenu: ConstraintLayout by lazy { findViewById(R.id.parentReport) }
    private val userMenu: ConstraintLayout by lazy { findViewById(R.id.parentUser) }
    private val name: TextView by lazy { findViewById(R.id.tvNameProfile) }
    private val position: TextView by lazy { findViewById(R.id.tvPositionProfile) }
    private val image: CircleImageView by lazy { findViewById(R.id.imgProfile) }
    private val setting: ImageView by lazy { findViewById(R.id.icSetting) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manager_main)
        sharedPref = PreferencesHelper(this)

        setting.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Aksi")

            val options = arrayOf("Lihat profil", "Logout")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        startActivity(
                            Intent(
                                this,
                                ProfileActivity::class.java
                            )
                        )
                    }
                    1 -> {
                        logout()
                    }
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

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

    override fun onResume() {
        super.onResume()

        user()
    }

    private fun user() {

        ApiClient.SetContext(this).instances.apiUser().enqueue(
            object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {

                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status
                        val user = response.body()?.user

                        if (status == true) {

                            name.text = user?.name
                            position.text = user?.position
                            Picasso.with(applicationContext)
                                .load("${Constant.URL_IMG_USER}${user?.image}")
                                .into(image)

                            Log.e(this.toString(), "onResponse: Sukses | " + user?.position)
                        } else {
                            Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal : " + response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Gagal : " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun logout() {

        ApiClient.SetContext(this).instances.apiLogout().enqueue(object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    val status = response.body()?.status

                    if (status == true) {
                        sharedPref.logout()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Gagal : " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Gagal : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

}