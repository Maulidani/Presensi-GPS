package com.skripsi.presensigps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.DataModel
import com.skripsi.presensigps.utils.PreferencesHelper


class PresenceAdapter(private val list: List<DataModel>) :
    RecyclerView.Adapter<PresenceAdapter.PresenceViewlHoder>() {

    inner class PresenceViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(result: DataModel) {

            val sharedPref: PreferencesHelper = PreferencesHelper(itemView.context)
//            val nameLogin = sharedPref.getString(Constant.PREF_USER_NAME)

            val statusVerified = itemView.findViewById<TextView>(R.id.tvStatusVerified)
            val statusNotVerified = itemView.findViewById<TextView>(R.id.tvStatusNotYetVerify)

            val img = itemView.findViewById<ImageView>(R.id.imgPresence)

            val name = itemView.findViewById<TextView>(R.id.tvName)
            val date = itemView.findViewById<TextView>(R.id.tvDate)
            val time = itemView.findViewById<TextView>(R.id.tvTime)

//            Picasso.with(itemView.context).load("${Constant.URL_IMG_PRESENCE}${result.image}")
//                .into(img)
////            name.text = nameLogin
//            date.text = result.created_at
//
//            if (result.status == 0) {
//                statusNotVerified.visibility = View.VISIBLE
//                statusVerified.visibility = View.INVISIBLE
//            } else {
//                statusNotVerified.visibility = View.INVISIBLE
//                statusVerified.visibility = View.VISIBLE
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenceViewlHoder {
        return PresenceViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_presence, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PresenceViewlHoder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}