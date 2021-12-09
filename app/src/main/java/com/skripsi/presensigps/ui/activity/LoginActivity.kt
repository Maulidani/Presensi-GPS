package com.skripsi.presensigps.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.network.UserModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val inputEmail: TextInputEditText by lazy { findViewById(R.id.inputEmail) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }

    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = PreferencesHelper(this)
        if (sharedPref.getBoolean(Constant.PREF_IS_LOGIN)) {
            Log.e(this.toString(), "processLogin: Anda sudah login...")
            finish()
        }

        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Email: tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Kata sandi: tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {

        ApiClient.instancesNoToken.apiLogin(email, password)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {

                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status
                        val token = response.body()?.token

                        if (status == true) {
                            if (token != null) {
                                sharedPref.put(Constant.PREF_USER_TOKEN, token)
                                Log.e(this.toString(), "Token: $token")

                                user()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Gagal : token is null",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
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
                            saveSession(user)
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

    private fun saveSession(user: UserModel?) {

        sharedPref.put(Constant.PREF_USER_ID, user!!.id.toString())
        sharedPref.put(Constant.PREF_USER_POSITION, user.position)
        sharedPref.put(Constant.PREF_IS_LOGIN, true)

        if (user.position == "admin" || user.position == "manager") {
            startActivity(
                Intent(
                    this,
                    AdminManagerMainActivity::class.java
                )
            )
        } else {
            startActivity(Intent(this, SalesMainActivity::class.java))
        }
        finish()
    }


}











