package com.skripsi.presensigps.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.model.SalesInfoModel
import com.skripsi.presensigps.ui.viewmodel.ScreenState
import com.skripsi.presensigps.ui.viewmodel.UserViewModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import com.squareup.picasso.Picasso
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
    private val viewModel: UserViewModel.SalesInfoViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel.SalesInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_main)
        sharedPref = PreferencesHelper(this)
        val userId = sharedPref.getString(Constant.PREF_USER_ID)
        val userToken = sharedPref.getString(Constant.PREF_USER_TOKEN)
        userId?.let { it1 -> userToken?.let { it2 -> viewModel.salesInfo(it2, it1) } }

        viewModel.salesInfoLiveData.observe(this, {
            processLogin(it)
            userId?.let { it1 -> userToken?.let { it2 -> viewModel.salesInfo(it2, it1) } }
        })

    }

    private fun processLogin(state: ScreenState<SalesInfoModel>) {

        when (state) {
            is ScreenState.Loading -> {
                Log.e(this.toString(), "processLogin: loading...")
            }
            is ScreenState.Success -> {
                if (state.data != null) {
                    // action when success
                    Log.e(this.toString(), "processLogin: Success!")
                    init(state.data)
                } else {
                    Log.e(this.toString(), "processLogin: Success Null!")
                }
            }
            is ScreenState.Error -> {
                Log.e(this.toString(), "processLogin: Failed!")
            }
        }
    }

    private fun init(data: SalesInfoModel) {

        btnReport.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MapsActivity::class.java
                ).putExtra("type", "report")
            )
        }

        icSetting.setOnClickListener {
            startActivity(
                Intent(this, ProfileActivity::class.java).putExtra(
                    "id",
                    sharedPref.getString(Constant.PREF_USER_ID)
                )
            )
        }

        tvAllHistory.setOnClickListener {
            startActivity(Intent(this, AllHistoryActivity::class.java))
        }

        val imgUserEndPoint = sharedPref.getString(Constant.URL_IMG_USER)

        Picasso.with(this).load(Constant.BASE_URL + imgUserEndPoint + data.user.image)
            .into(imgProfile)

        tvNameProfile.text = data.user.name
        tvPositionProfile.text = data.user.position

        if (data.count.presence_this_month.toString().isNotEmpty()) {
            tvCountPresence.text = data.count.presence_this_month.toString()
        }

        if (data.count.report_this_month.toString().isNotEmpty()) {
            tvCountReport.text = data.count.report_this_month.toString()
        }

        if (data.presence != null) {

            btnPresence.visibility = View.INVISIBLE
            parentDetailPresence.visibility = View.VISIBLE
            tvNotYetPresence.visibility = View.GONE

            tvDatePresence.text = data.presence.created_at

            if (data.presence.status == 1) {
                tvStatusPresence.text = data.presence.status.toString()
            } else {
                tvStatusPresence.text = data.presence.status.toString()
            }

            if (data.presence.back == 0) {
                btnPresenceBack.visibility = View.VISIBLE
                tvPresenceBack.visibility = View.GONE

                btnPresenceBack.setOnClickListener {
                    viewModel.salesInfoLiveData.observe(this, {
                        viewModel.presenceBack(data.presence.id.toString())
                    })
                }

            } else {
                btnPresenceBack.visibility = View.GONE
                tvPresenceBack.visibility = View.VISIBLE

                btnPresence.setOnClickListener {
                    startActivity(
                        Intent(
                            this,
                            MapsActivity::class.java
                        ).putExtra("type", "presence")
                            .putExtra(
                                "latitude",
                                sharedPref.getString(Constant.PREF_OFFICE_LOCATION_LATITUDE)
                            )
                            .putExtra(
                                "longitude",
                                sharedPref.getString(Constant.PREF_OFFICE_LOCATION_LONGITUDE)
                            )
                            .putExtra(
                                "radius",
                                sharedPref.getString(Constant.PREF_OFFICE_LOCATION_RADIUS)
                            )
                    )
                }

            }

        } else {

            btnPresence.visibility = View.VISIBLE
            parentDetailPresence.visibility = View.GONE
            tvNotYetPresence.visibility = View.VISIBLE
        }

    }

}