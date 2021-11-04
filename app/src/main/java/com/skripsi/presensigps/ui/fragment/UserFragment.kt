package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.skripsi.presensigps.R

class UserFragment(val s:String) : Fragment() {

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
            "sales" -> Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show()
            "manager" -> Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show()
        }
    }
}