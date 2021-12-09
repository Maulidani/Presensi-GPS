package com.skripsi.presensigps.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import de.hdodenhof.circleimageview.CircleImageView

class SalesMainActivity : AppCompatActivity() {
    private val icSetting: ImageView by lazy { findViewById(R.id.icSetting) }
    private val imgProfile: CircleImageView by lazy { findViewById(R.id.imgProfile) }
    private val tvNameProfile: TextView by lazy { findViewById(R.id.tvNameProfile) }
    private val tvPositionProfile: TextView by lazy { findViewById(R.id.tvPositionProfile) }
    private val tvAllHistory: TextView by lazy { findViewById(R.id.tvAllHistory) }
    private val tvCountPresence: TextView by lazy { findViewById(R.id.tvCountPresence) }
    private val tvCountReport: TextView by lazy { findViewById(R.id.tvCountReport) }
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



    }

//    private fun init(data: DataModel) {
//
//        btnReport.setOnClickListener {
//            startActivity(
//                Intent(
//                    this,
//                    MapsActivity::class.java
//                ).putExtra("type", "report")
//            )
//        }
//
//        icSetting.setOnClickListener {
//            startActivity(
//                Intent(this, ProfileActivity::class.java).putExtra(
//                    "id",
//                    sharedPref.getString(Constant.PREF_USER_ID)
//                )
//            )
//        }
//
//        tvAllHistory.setOnClickListener {
//            startActivity(Intent(this, AllHistoryActivity::class.java))
//        }
//
//
//        Picasso.with(this).load("${Constant.URL_IMG_PRESENCE}${data.image}")
//            .into(imgProfile)
//
//
//        tvNameProfile.text = data.
//        tvPositionProfile.text = data.
//
//        if (data != null) {
//
//            btnPresence.visibility = View.INVISIBLE
//            parentDetailPresence.visibility = View.VISIBLE
//            tvNotYetPresence.visibility = View.GONE
//
//            tvDatePresence.text = data.
//
//            if (data.presence.status == 1) {
//                tvStatusPresence.text = data.
//            } else {
//                tvStatusPresence.text = data.
//            }
//
//            if (data.back == 0) {
//                btnPresenceBack.visibility = View.VISIBLE
//                tvPresenceBack.visibility = View.GONE
//
//                btnPresenceBack.setOnClickListener {
//                    viewModel.salesInfoLiveData.observe(this, {
//                        viewModel.presenceBack(data.presence.id.toString())
//                    })
//                }
//
//            } else {
//                btnPresenceBack.visibility = View.GONE
//                tvPresenceBack.visibility = View.VISIBLE
//
//            }
//
//        } else {
//
//            btnPresenceBack.visibility = View.GONE
//            btnPresence.visibility = View.VISIBLE
//            parentDetailPresence.visibility = View.GONE
//            tvNotYetPresence.visibility = View.VISIBLE
//
//            btnPresence.setOnClickListener {
//                startActivity(
//                    Intent(
//                        this,
//                        MapsActivity::class.java
//                    ).putExtra("type", "presence")
//                        .putExtra(
//                            "latitude",
//                            sharedPref.getString(Constant.PREF_OFFICE_LOCATION_LATITUDE)
//                        )
//                        .putExtra(
//                            "longitude",
//                            sharedPref.getString(Constant.PREF_OFFICE_LOCATION_LONGITUDE)
//                        )
//                        .putExtra(
//                            "radius",
//                            sharedPref.getString(Constant.PREF_OFFICE_LOCATION_RADIUS)
//                        )
//                )
//            }
//        }
//    }

}