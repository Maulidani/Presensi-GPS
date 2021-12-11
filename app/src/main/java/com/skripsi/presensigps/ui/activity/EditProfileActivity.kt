package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private val inputName: TextInputEditText by lazy { findViewById(R.id.inputName) }
    private val inputPosition: AutoCompleteTextView by lazy { findViewById(R.id.inputPosition) }
    private val inputEmail: TextInputEditText by lazy { findViewById(R.id.inputEmail) }
    private val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    private val btnEdit: MaterialButton by lazy { findViewById(R.id.btnEdit) }
    private val back: ImageView by lazy { findViewById(R.id.icBack) }
    private var positionList = listOf(
        "sales", "manager"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPref = PreferencesHelper(this)
        val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

        val intentId = intent.getIntExtra("id", 0)
        val intentName = intent.getStringExtra("name")
        val intentPosition = intent.getStringExtra("position")
        val intentEmail = intent.getStringExtra("email")
        val intentPassword = intent.getStringExtra("password")

        inputName.setText(intentName)
        inputPosition.setText(intentPosition)
        inputEmail.setText(intentEmail)
        inputPassword.setText(intentPassword)

        if (intentPosition != "admin") {
            val adapterPosition =
                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, positionList)
            inputPosition.setAdapter(adapterPosition)
        }

        back.setOnClickListener { finish() }

        btnEdit.setOnClickListener {
            val name = inputName.text.toString()
            val position = inputPosition.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (name.isNotEmpty() && position.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

                editProfile(intentId, name, position, email, password)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Data tidak boleh ada yang kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editProfile(
        id: Int,
        name: String?,
        position: String?,
        email: String?,
        password: String?
    ) {

        ApiClient.SetContext(this).instances.apiEditUser(
            id,
            name!!,
            position!!,
            email!!,
            password!!
        )
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status

                        if (status == true) {
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