package com.skripsi.presensigps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.network.model.DataPresenceModel
import com.skripsi.presensigps.network.model.PresenceIndexModel


class PresenceAdapter(private val characterList: List<DataPresenceModel>) :
    RecyclerView.Adapter<PresenceAdapter.PresenceViewlHoder>() {

    inner class PresenceViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(result: DataPresenceModel) {
            val name = itemView.findViewById<TextView>(R.id.tvName)

            name.text = result.created_at

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenceViewlHoder {
        return PresenceViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_presence, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PresenceViewlHoder, position: Int) {
        holder.bindData(characterList[position])
    }

    override fun getItemCount(): Int = characterList.size
}