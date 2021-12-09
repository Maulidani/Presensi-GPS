package com.skripsi.presensigps.ui.activity

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddUserActivity : AppCompatActivity() {
    val back: ImageView by lazy { findViewById(R.id.icBack) }
    val inputName: TextInputEditText by lazy { findViewById(R.id.inputName) }
    val inputPosition: AutoCompleteTextView by lazy { findViewById(R.id.inputPosition) }
    val inputEmail: TextInputEditText by lazy { findViewById(R.id.inputEmail) }
    val inputPassword: TextInputEditText by lazy { findViewById(R.id.inputPassword) }
    val btnAdd: MaterialButton by lazy { findViewById(R.id.btnAdd) }
    val btnChoose: MaterialButton by lazy { findViewById(R.id.btnChoosePhoto) }
    val imgPhoto: ImageView by lazy { findViewById(R.id.imgPhoto) }
    private var positionList = listOf(
        "sales", "manager"
    )
    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val adapterPosition =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, positionList)
        inputPosition.setAdapter(adapterPosition)

        back.setOnClickListener { finish() }

        btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        btnAdd.setOnClickListener {
            val name = inputName.text.toString()
            val position = inputPosition.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            if (name.isNotEmpty() && position.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && partImage != null) {

                registration(name, position, email, password)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Data tidak boleh ada yang kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registration(name: String, position: String, email: String, password: String) {

        val partName: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPosition: RequestBody = position.toRequestBody("text/plain".toMediaTypeOrNull())
        val partEmail: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val partPassword: RequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.SetContext(this).instances.apiAdduser(
            partName,
            partPosition,
            partEmail,
            partPassword,
            partImage!!
        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
//                    imageView.setImageURI(fileUri)

                val image: File = File(fileUri.path!!)
                imgPhoto.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())

                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

}