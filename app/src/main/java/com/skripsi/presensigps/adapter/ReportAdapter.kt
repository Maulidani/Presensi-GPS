package com.skripsi.presensigps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.DataModel
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.ui.activity.MapsInfoActivity
import com.skripsi.presensigps.ui.activity.PhotoActivity
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class ReportAdapter(
    private val list: List<DataModel>,
    private val mListener: ReportAdapter.IUserRecycler
) :
    RecyclerView.Adapter<ReportAdapter.ReportViewlHoder>() {

    inner class ReportViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(result: DataModel) {

            val sharedPref: PreferencesHelper = PreferencesHelper(itemView.context)
            val userPosition = sharedPref.getString(Constant.PREF_USER_POSITION)

            val name = itemView.findViewById<TextView>(R.id.tvName)
            val date = itemView.findViewById<TextView>(R.id.tvDate)
            val time = itemView.findViewById<TextView>(R.id.tvTime)
            val statusVerified = itemView.findViewById<TextView>(R.id.tvStatusVerified)
            val statusNotVerified = itemView.findViewById<TextView>(R.id.tvStatusNotYetVerify)
            val latitude = itemView.findViewById<TextView>(R.id.tvLatitudo)
            val longitude = itemView.findViewById<TextView>(R.id.tvLongitude)
            val note = itemView.findViewById<TextView>(R.id.tvNote)
            val img = itemView.findViewById<ImageView>(R.id.imgReport)
            val item = itemView.findViewById<CardView>(R.id.itemCard)

            val timestampCreatedAt: Timestamp = result.created_at
            val dateCreatedAt = Date(timestampCreatedAt.time)

            val simpleDateFormat =
                SimpleDateFormat("MM/dd/yyyy")

            val simpleDateFormatTime =
                SimpleDateFormat("HH:mm:ss")

            Picasso.with(itemView.context).load("${Constant.URL_IMG_REPORT}${result.image}")
                .into(img)
            name.text = result.name
            date.text = simpleDateFormat.format(dateCreatedAt).toString()
            time.text = simpleDateFormatTime.format(dateCreatedAt).toString()
            latitude.text = result.latitude.toString()
            longitude.text = result.longitude.toString()
            note.text = result.note

            latitude.setOnClickListener {
                ContextCompat.startActivity(
                    itemView.context, Intent(itemView.context, MapsInfoActivity::class.java)
                        .putExtra("type", "detail_report")
                        .putExtra("latitude", latitude.text.toString())
                        .putExtra("longitude", longitude.text.toString()), null
                )
            }
            longitude.setOnClickListener {
                ContextCompat.startActivity(
                    itemView.context, Intent(itemView.context, MapsInfoActivity::class.java)
                        .putExtra("type", "detail_report")
                        .putExtra("latitude", latitude.text.toString())
                        .putExtra("longitude", longitude.text.toString()), null
                )
            }

            img.setOnClickListener {
                ContextCompat.startActivity(
                    itemView.context,
                    Intent(itemView.context, PhotoActivity::class.java)
                        .putExtra("image", "${Constant.URL_IMG_REPORT}${result.image}"), null
                )
            }

            if (result.status == 0) {
                statusNotVerified.visibility = View.VISIBLE
                statusVerified.visibility = View.INVISIBLE

                if (userPosition == "manager") {

                    item.setOnClickListener {
                        verificationAlert(result)
                    }

                }
            } else {
                statusNotVerified.visibility = View.INVISIBLE
                statusVerified.visibility = View.VISIBLE
            }


        }

        private fun verificationAlert(result: DataModel) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Verifikasi")
            builder.setMessage("verifikasi Laporan ${result.name} ?")

            builder.setPositiveButton("Ya") { _, _ ->
                verify(result)
            }

            builder.setNegativeButton("Tidak") { _, _ ->
                // cancel
            }
            builder.show()
        }

        private fun verify(result: DataModel) {
            ApiClient.SetContext(itemView.context).instances.apiVerifyReport(result.id, 1)
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
                                mListener.refreshView(true)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewlHoder {
        return ReportViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReportViewlHoder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface IUserRecycler {
        fun refreshView(onUpdate: Boolean)
    }
}