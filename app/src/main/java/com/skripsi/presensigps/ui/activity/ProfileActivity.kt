package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.model.DataUserModel
import com.skripsi.presensigps.network.model.UserModel
import com.skripsi.presensigps.ui.viewmodel.ScreenState
import com.skripsi.presensigps.ui.viewmodel.UserViewModel
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {
    private val img: CircleImageView by lazy { findViewById(R.id.imgProfile) }
    private val tvName: TextView by lazy { findViewById(R.id.tvName) }
    private val tvPosition: TextView by lazy { findViewById(R.id.tvPosition) }
    private val tvEmail: TextView by lazy { findViewById(R.id.tvEmail) }
    private val tvpassword: TextView by lazy { findViewById(R.id.tvPassword) }

    private lateinit var sharedPref: PreferencesHelper
    private val viewModel: UserViewModel.UserInfoViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel.UserInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sharedPref = PreferencesHelper(this)
        val token = sharedPref.getString(Constant.PREF_USER_TOKEN)

        viewModel.user(token.toString())
        viewModel.userLiveData.observe(this, {
            process(it)
            viewModel.user(token.toString())
        })

    }

    private fun process(state: ScreenState<UserModel>) {

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

    private fun init(data: UserModel) {

        val imgUserEndPoint = sharedPref.getString(Constant.URL_IMG_USER)

        Picasso.with(this).load(Constant.BASE_URL + imgUserEndPoint + data.data.image)
            .into(img)

        tvName.text = data.data.name
        tvPosition.text = data.data.position
        tvEmail.text = data.data.email
        tvpassword.text = data.data.password
    }

}