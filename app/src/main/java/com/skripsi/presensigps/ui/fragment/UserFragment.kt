package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.adapter.UserAdapter
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment(val s: String) : Fragment() {
    private val rv: RecyclerView by lazy { requireActivity().findViewById(R.id.rvUser) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (s) {
            "sales" -> showUser("sales")
            "manager" -> showUser("manager")
        }
    }

    override fun onResume() {
        super.onResume()

        when (s) {
            "sales" -> showUser("sales")
            "manager" -> showUser("manager")
        }
    }

    private fun showUser(s: String) {

        ApiClient.SetContext(requireContext()).instances.apiShowUser(s).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    val status = response.body()?.status
                    val user = response.body()?.list_user

                    if (status == true) {

                        val adapter = user?.let { UserAdapter(it) }
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
}