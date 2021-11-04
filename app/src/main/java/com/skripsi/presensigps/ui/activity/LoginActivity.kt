package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.model.LoginModel
import com.skripsi.presensigps.network.model.SalesInfoModel
import com.skripsi.presensigps.ui.viewmodel.ScreenState
import com.skripsi.presensigps.ui.viewmodel.UserViewModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper

class LoginActivity : AppCompatActivity() {

    private val inputEmail: TextInputEditText by lazy { findViewById(R.id.inputEmail) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    private val btnLogin: MaterialButton by lazy { findViewById(R.id.btnLogin) }

    private lateinit var sharedPref: PreferencesHelper
    private val viewModel: UserViewModel.LoginViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel.LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = PreferencesHelper(this)
        if (sharedPref.getBoolean(Constant.PREF_IS_LOGIN)) {
            Log.e(this.toString(), "processLogin: Anda sudah login...")
            finish()
        }
        viewModel.loginLiveData.observe(this, {
            processLogin(it)
        })

        btnLogin.setOnClickListener {
            if (inputEmail.text.toString().isEmpty()) {
                Log.e(this.toString(), "email: tidak boleh kosong")
            } else if (inputPassword.text.toString().isEmpty()) {
                Log.e(this.toString(), "password: tidak boleh kosong")
            } else {
                viewModel.login(inputEmail.text.toString(), inputPassword.text.toString())
            }
        }
    }

    private fun processLogin(state: ScreenState<LoginModel>) {

        when (state) {
            is ScreenState.Loading -> {
                Log.e(this.toString(), "processLogin: loading...")
            }
            is ScreenState.Success -> {
                if (state.data != null) {
                    // action when success
                    Log.e(this.toString(), "processLogin: Success!")
                    saveSession(state.data)
                } else {
                    Log.e(this.toString(), "processLogin: Success Null!")
                }
            }
            is ScreenState.Error -> {
                Log.e(this.toString(), "processLogin: Failed!")
            }
        }
    }

    private fun saveSession(data: LoginModel) {

        sharedPref.put(Constant.PREF_USER_TOKEN, data.data.token)
        sharedPref.put(Constant.PREF_USER_ID, data.data.id.toString())
        sharedPref.put(Constant.PREF_USER_NAME, data.data.name)
        sharedPref.put(Constant.PREF_USER_POSITION, data.data.position)
        sharedPref.put(Constant.PREF_IS_LOGIN, true)

        sharedPref.put(Constant.URL_IMG_USER, data.link_image.user)
        sharedPref.put(Constant.URL_IMG_PRESENCE, data.link_image.presence)
        sharedPref.put(Constant.URL_IMG_REPORT, data.link_image.report)

        sharedPref.put(Constant.PREF_OFFICE_LOCATION_LATITUDE, data.office.latitude.toString())
        sharedPref.put(Constant.PREF_OFFICE_LOCATION_LONGITUDE, data.office.longitude.toString())
        sharedPref.put(Constant.PREF_OFFICE_LOCATION_RADIUS, data.office.radius.toString())

        if (data.data.position == "admin" || data.data.position == "manager") {
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











