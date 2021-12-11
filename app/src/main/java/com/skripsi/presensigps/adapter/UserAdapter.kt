package com.skripsi.presensigps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.network.UserModel
import com.skripsi.presensigps.ui.activity.ProfileActivity
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAdapter(private val list: List<UserModel>) :
    RecyclerView.Adapter<UserAdapter.PresenceViewlHoder>() {

    inner class PresenceViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(result: UserModel) {

            val item = itemView.findViewById<CardView>(R.id.itemCard)
            val name = itemView.findViewById<TextView>(R.id.tvName)

            name.text = result.name


            val sharedPref: PreferencesHelper = PreferencesHelper(itemView.context)
            val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)
            if (userPosition == "admin") {
                item.setOnClickListener {
                    optionAlert(result)
                }
            } else {
                item.setOnClickListener {
                    startActivity(
                        itemView.context,
                        Intent(itemView.context, ProfileActivity::class.java)
                            .putExtra("id", result.id)
                            .putExtra("name", result.name)
                            .putExtra("position", result.position)
                            .putExtra("email", result.email)
                            .putExtra("password", result.password)
                            .putExtra("image", result.image), null
                    )
                }
            }
        }

        private fun optionAlert(result: UserModel) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Aksi")

            val options = arrayOf("Lihat profil", "Hapus user ini")
            builder.setItems(
                options
            ) { _, which ->
                when (which) {
                    0 -> {
                        startActivity(
                            itemView.context,
                            Intent(itemView.context, ProfileActivity::class.java)
                                .putExtra("id", result.id)
                                .putExtra("name", result.name)
                                .putExtra("position", result.position)
                                .putExtra("email", result.email)
                                .putExtra("password", result.password)
                                .putExtra("image", result.image), null
                        )
                    }
                    1 -> deleteAlert(result)
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        private fun deleteAlert(result: UserModel) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Hapus")
            builder.setMessage("Hapus User ${result.name} ?")

            builder.setPositiveButton("Ya") { _, _ ->
                delete(result)
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // cancel
            }
            builder.show()
        }

        private fun delete(result: UserModel) {
            ApiClient.SetContext(itemView.context).instances.apiDeleteUser(result.id)
                .enqueue(object :
                    Callback<ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            val message = response.body()?.message
                            val status = response.body()?.status

                            if (status == true) {
                                //success
                            } else {
                                Toast.makeText(itemView.context, "Gagal", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                itemView.context,
                                "Gagal : " + response.code().toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Toast.makeText(
                            itemView.context,
                            "Gagal : " + t.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenceViewlHoder {
        return PresenceViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PresenceViewlHoder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}