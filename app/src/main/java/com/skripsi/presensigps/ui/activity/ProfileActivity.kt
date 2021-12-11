package com.skripsi.presensigps.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
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

class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val back: ImageView by lazy { findViewById(R.id.icBack) }
    private val dataProfile: LinearLayoutCompat by lazy { findViewById(R.id.parentDataProfile) }
    private val img: CircleImageView by lazy { findViewById(R.id.imgProfile) }
    private val tvName: TextView by lazy { findViewById(R.id.tvName) }
    private val tvPosition: TextView by lazy { findViewById(R.id.tvPosition) }
    private val tvEmail: TextView by lazy { findViewById(R.id.tvEmail) }
    private val tvPassword: TextView by lazy { findViewById(R.id.tvPassword) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPref = PreferencesHelper(this)

        val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

        val intentId = intent.getIntExtra("id", 0)
        val intentName = intent.getStringExtra("name")
        val intentPosition = intent.getStringExtra("position")
        val intentEmail = intent.getStringExtra("email")
        val intentPassword = intent.getStringExtra("password")
        val intentImg = intent.getStringExtra("image")

        if (intentId == 0) {
            user()

        } else {

            tvName.text = intentName
            tvPosition.text = intentPosition
            tvEmail.text = intentEmail
            tvPassword.text = intentPassword
            Picasso.with(applicationContext)
                .load("${Constant.URL_IMG_USER}${intentImg}")
                .into(img)



            if (userPosition == "admin") {
                img.setOnClickListener {
                    if (intentImg != null) {
                        optionAlert(intentImg, intentId.toString())
                    }

                }

                dataProfile.setOnClickListener {
                    startActivity(
                        Intent(
                            applicationContext,
                            EditProfileActivity::class.java
                        )
                            .putExtra("id", intentId)
                            .putExtra("name", intentName)
                            .putExtra("position", intentPosition)
                            .putExtra("email", intentEmail)
                            .putExtra("password", intentPassword)
                    )
                }
            }
        }

        back.setOnClickListener { finish() }

    }

    override fun onResume() {
        super.onResume()

        val intentId = intent.getIntExtra("id", 0)
        if (intentId == 0) {
            user()
        }
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

                            tvName.text = user?.name
                            tvPosition.text = user?.position
                            tvEmail.text = user?.email
                            tvPassword.text = user?.password
                            Picasso.with(applicationContext)
                                .load("${Constant.URL_IMG_USER}${user?.image}")
                                .into(img)

                            Log.e(this.toString(), "onResponse: Sukses | " + user?.position)

                            if (user != null) {
                                val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

                                if (userPosition == "admin") {
                                    val userId = sharedPref.getString(Constant.PREF_USER_ID)

                                    img.setOnClickListener {

                                        optionAlert(user.image, userId!!)
                                    }
                                    dataProfile.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                EditProfileActivity::class.java
                                            )
                                                .putExtra("id", user.id)
                                                .putExtra("name", user.name)
                                                .putExtra("position", user.position)
                                                .putExtra("email", user.email)
                                                .putExtra("password", user.password)
                                        )
                                    }
                                } else {
                                    img.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                applicationContext,
                                                PhotoActivity::class.java
                                            ).putExtra(
                                                "image",
                                                "${Constant.URL_IMG_USER}${user.image}"
                                            )
                                        )
                                    }
                                }
                            }
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

    private fun optionAlert(img: String, userId: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Aksi")

        val options = arrayOf("Lihat foto", "Ganti foto")
        builder.setItems(
            options
        ) { _, which ->
            when (which) {
                0 -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            PhotoActivity::class.java
                        ).putExtra("image", "${Constant.URL_IMG_USER}$img")
                    )
                }
                1 -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            EditPhotoActivity::class.java
                        ).putExtra("image", img).putExtra("id", userId)
                    )
                }
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

//    private fun deleteAlert(itemView: View, id: Int, token: String?) {
//        val builder = AlertDialog.Builder(itemView.context)
//        builder.setTitle("Hapus")
//        builder.setMessage("Hapus produk ini ?")
//
//        builder.setPositiveButton("Ya") { _, _ ->
//            delete(itemView, id, token)
//        }
//
//        builder.setNegativeButton("Tidak") { _, _ ->
//            // cancel
//        }
//        builder.show()
//    }
}