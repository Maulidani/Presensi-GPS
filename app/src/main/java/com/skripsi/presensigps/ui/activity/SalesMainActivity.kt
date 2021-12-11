package com.skripsi.presensigps.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class SalesMainActivity : AppCompatActivity() {
    private val setting: ImageView by lazy { findViewById(R.id.icSetting) }
    private val image: CircleImageView by lazy { findViewById(R.id.imgProfile) }
    private val name: TextView by lazy { findViewById(R.id.tvNameProfile) }
    private val position: TextView by lazy { findViewById(R.id.tvPositionProfile) }
    private val tvInfoPresence: TextView by lazy { findViewById(R.id.tvInfoPresence) }
    private val tvNotYetPresence: TextView by lazy { findViewById(R.id.tvNotYetPresence) }
    private val parentDetailPresence: ConstraintLayout by lazy { findViewById(R.id.parentDetailPresence) }
    private val tvDatePresence: TextView by lazy { findViewById(R.id.tvDatePresence) }
    private val tvTimePresence: TextView by lazy { findViewById(R.id.tvTimePresence) }
    private val tvStatusPresence: TextView by lazy { findViewById(R.id.tvStatusPresence) }
    private val tvPresenceBack: TextView by lazy { findViewById(R.id.tvPresenceBack) }
    private val btnPresence: MaterialButton by lazy { findViewById(R.id.btnPresence) }
    private val btnPresenceBack: MaterialButton by lazy { findViewById(R.id.btnPresenceBack) }
    private val btnReport: MaterialButton by lazy { findViewById(R.id.btnReport) }

    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_main)
        sharedPref = PreferencesHelper(this)
        val userToken = sharedPref.getString(Constant.PREF_USER_TOKEN)

        setting.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Aksi")

            val options = arrayOf("Riwayat Presensi/Laporan", "Lihat profil", "Logout")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        startActivity(
                            Intent(
                                this,
                                AllHistoryActivity::class.java
                            )
                        )
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this,
                                ProfileActivity::class.java
                            )
                        )
                    }
                    2 -> {
                        logout()
                    }
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
        btnReport.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MapsActivity::class.java
                ).putExtra("type", "report")
            )
        }

        getPresenceToday()
    }

    override fun onResume() {
        super.onResume()

        user()
        getPresenceToday()

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

    private fun getPresenceToday() {

        ApiClient.SetContext(this).instances.apiGetPresenceToday()
            .enqueue(object : Callback<ResponseModel> {
                @SuppressLint("SimpleDateFormat")
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status
                        val data = response.body()?.data_today

                        if (status == true) {

                            if (data != null) {

                                btnPresence.visibility = View.INVISIBLE
                                parentDetailPresence.visibility = View.VISIBLE
                                tvNotYetPresence.visibility = View.GONE

                                val timestampCreatedAt: Timestamp = data.created_at
                                val dateCreatedAt = Date(timestampCreatedAt.time)

                                val simpleDateFormat =
                                    SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss:S")

                                tvDatePresence.text =
                                    simpleDateFormat.format(dateCreatedAt).toString()

                                if (data.off == 1) {
                                    tvInfoPresence.text = "Anda telah mengirim off/izin hari ini"
                                    tvPresenceBack.visibility = View.GONE

                                    if (data.status == 1) {
                                        tvStatusPresence.text = "Izin Anda belum diverifikasi"
                                    } else {
                                        tvStatusPresence.text = "Izin Anda telah diverifikasi"
                                    }
                                }


                                if (data.back_at == null) {
                                    btnPresenceBack.visibility = View.VISIBLE
                                    tvPresenceBack.visibility = View.GONE
                                    tvStatusPresence.visibility = View.GONE

                                    btnPresenceBack.setOnClickListener {
                                        presenceback()
                                    }

                                } else {

                                    val timestampCreatedAt: Timestamp = data.created_at
                                    val dateCreatedAt = Date(timestampCreatedAt.time)

                                    val simpleDateFormat =
                                        SimpleDateFormat("H")

                                    val time = data.back_at - simpleDateFormat.format(dateCreatedAt)
                                        .toInt()
                                    tvTimePresence.text = time.toString()

                                    btnPresenceBack.visibility = View.GONE
                                    tvStatusPresence.visibility = View.GONE

                                }

                            }
                        } else {
                            btnPresenceBack.visibility = View.GONE
                            btnPresence.visibility = View.VISIBLE
                            parentDetailPresence.visibility = View.GONE
                            tvNotYetPresence.visibility = View.VISIBLE

                            btnPresence.setOnClickListener {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        MapsActivity::class.java
                                    ).putExtra("type", "presence")
                                )
                            }
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

    private fun presenceback() {

        ApiClient.SetContext(this).instances.apiBackPresence()
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status

                        if (status == true) {
                            btnPresenceBack.visibility = View.GONE

                            getPresenceToday()
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