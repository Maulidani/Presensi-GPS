package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.utils.PreferencesHelper

class AllHistoryFragment(val s: String) : Fragment() {
    private val rv: RecyclerView by lazy { requireActivity().findViewById(R.id.rv) }
    private lateinit var sharedPref: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = PreferencesHelper(requireContext())
        when (s) {
            "presence" -> {
            }
            "report" -> {
            }
        }
    }

}