package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.utils.Constant
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PhotoActivity : AppCompatActivity() {
    private val img: ImageView by lazy { findViewById(R.id.imgPhoto) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val intentImg = intent.getStringExtra("image")

        Picasso.with(applicationContext)
            .load(intentImg)
            .into(img)
    }
}