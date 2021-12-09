package com.skripsi.presensigps.ui.activity

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.utils.Constant
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditPhotoActivity : AppCompatActivity() {
    private val back: ImageView by lazy { findViewById(R.id.icBack) }
    private val img: ImageView by lazy { findViewById(R.id.imgPhoto) }
    private val btnChoose: MaterialButton by lazy { findViewById(R.id.btnChoosePhoto) }
    private val btnEdit: MaterialButton by lazy { findViewById(R.id.btnEdit) }

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo)

        val intentId = intent.getStringExtra("id")
        val intentImg = intent.getStringExtra("image")

        Picasso.with(applicationContext)
            .load("${Constant.URL_IMG_USER}${intentImg}")
            .into(img)

        back.setOnClickListener { finish() }

        btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        btnEdit.setOnClickListener {

            if (partImage != null) {
                editImg(intentId)
            } else {
                Toast.makeText(applicationContext, "Pilih foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editImg(id: String?) {
        val partId: RequestBody = id!!.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.SetContext(this).instances.apiEditImgUser(partId, partImage!!).enqueue(object :
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
//                    imageView.setImageURI(fileUri)

                val image: File = File(fileUri.path!!)
                img.setImageBitmap(BitmapFactory.decodeFile(image.absolutePath))

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