package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.adapter.PresenceAdapter
import com.skripsi.presensigps.adapter.ReportAdapter
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresenceFragment(val s: String) : Fragment(), PresenceAdapter.IUserRecycler {
    private val rv: RecyclerView by lazy { requireActivity().findViewById(R.id.rvPresence) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_presence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        show()
    }

    override fun onResume() {
        super.onResume()
        show()

    }

    private fun show() {

        ApiClient.SetContext(requireContext()).instances.apiShowPresence().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    val status = response.body()?.status
                    val data = response.body()?.data

                    if (status == true) {

                        val adapter = data?.let { PresenceAdapter(it,this@PresenceFragment) }
                        rv.layoutManager = LinearLayoutManager(requireContext())
                        rv.adapter = adapter

                    } else {
                        Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal : " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Gagal : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    override fun refreshView(onUpdate: Boolean) {
        show()
    }
}