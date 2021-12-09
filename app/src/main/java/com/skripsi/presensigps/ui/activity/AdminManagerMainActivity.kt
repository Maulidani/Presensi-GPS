package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.utils.Constant
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminManagerMainActivity : AppCompatActivity() {
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

        setting.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
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

}