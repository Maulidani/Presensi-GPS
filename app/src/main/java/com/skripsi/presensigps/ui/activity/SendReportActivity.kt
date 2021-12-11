package com.skripsi.presensigps.ui.activity

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
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

class SendReportActivity : AppCompatActivity() {
    val btnSendReport: MaterialButton by lazy { findViewById(R.id.btnSendReport) }
    val nameLoc: TextInputEditText by lazy { findViewById(R.id.inputName) }
    val note: TextInputEditText by lazy { findViewById(R.id.inputNote) }
    val lat: TextView by lazy { findViewById(R.id.tvLatitude) }
    val long: TextView by lazy { findViewById(R.id.tvLongitude) }

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_report)

        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        lat.text = latitude.toString()
        long.text = longitude.toString()

        btnSendReport.setOnClickListener {
            if (!nameLoc.text.isNullOrEmpty()) {
                ImagePicker.with(this)
                    .cameraOnly()
                    .cropSquare()
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Nama lokasi tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())

                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)

                sendReport()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun sendReport() {

        val partlatitude: RequestBody =
            latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val partLongitude: RequestBody =
            longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val partNameLoc: RequestBody =
            nameLoc.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val partNote: RequestBody =
            note.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.SetContext(this).instances.apiAddReport(
            partNameLoc,
            partlatitude,
            partLongitude,
            partNote,
            partImage!!
        ).enqueue(object :
            Callback<ResponseModel> {
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
}