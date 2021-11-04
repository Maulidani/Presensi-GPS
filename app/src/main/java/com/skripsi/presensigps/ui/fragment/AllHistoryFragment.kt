package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.presensigps.R
import com.skripsi.presensigps.adapter.PresenceAdapter
import com.skripsi.presensigps.adapter.ReportAdapter
import com.skripsi.presensigps.network.model.PresenceIndexModel
import com.skripsi.presensigps.network.model.ReportIndexModel
import com.skripsi.presensigps.ui.viewmodel.PresenceReportViewModel
import com.skripsi.presensigps.ui.viewmodel.ScreenState
import com.skripsi.presensigps.utils.Constant
import com.skripsi.presensigps.utils.PreferencesHelper

class AllHistoryFragment(val s: String) : Fragment() {
    private val rv: RecyclerView by lazy { requireActivity().findViewById(R.id.rv) }

    var boolPresence = true
    var boolReport = true

    private lateinit var sharedPref: PreferencesHelper
    private val viewModel: PresenceReportViewModel.PresenceReportUser by lazy {
        ViewModelProvider(this).get(PresenceReportViewModel.PresenceReportUser::class.java)
    }

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
        val userId = sharedPref.getString(Constant.PREF_USER_ID)

        when (s) {
            "presence" -> {
                if (s == "presence") viewModel.myPresence(userId.toString())
                if (s == "presence") viewModel.presenceLiveData.observe(requireActivity(), {
                    processMyPresence(it)
                    if (s == "presence") viewModel.myPresence(userId.toString())

                })
            }
            "report" -> {
                if (s == "report") viewModel.myReport(userId.toString())
                if (s == "report") viewModel.reportLiveData.observe(requireActivity(), {
                    processMyReport(it)
                    if (s == "report") viewModel.myReport(userId.toString())
                })
            }
        }
    }

    private fun processMyPresence(state: ScreenState<PresenceIndexModel>) {

        when (state) {
            is ScreenState.Loading -> {
                Log.e(this.toString(), "processLogin: loading...")
            }
            is ScreenState.Success -> {
                if (state.data != null) {
                    // action when success
                    Log.e(this.toString(), "processLogin: Success!")

                    if (boolPresence) {
                        val adapter = PresenceAdapter(state.data.data)
                        rv.layoutManager = LinearLayoutManager(requireActivity())
                        rv.adapter = adapter

                        boolPresence = false
                    }
                } else {
                    Log.e(this.toString(), "processLogin: Success Null!")
                }
            }
            is ScreenState.Error -> {
                Log.e(this.toString(), "processLogin: Failed!")
            }
        }
    }

    private fun processMyReport(state: ScreenState<ReportIndexModel>) {

        when (state) {
            is ScreenState.Loading -> {
                Log.e(this.toString(), "processLogin: loading...")
            }
            is ScreenState.Success -> {
                if (state.data != null) {
                    // action when success
                    Log.e(this.toString(), "processLogin: Success!")

                    Log.e(this.toString(), "processLogin: Success!")

                    if (boolReport) {
                        val adapter = ReportAdapter(state.data.data)
                        rv.layoutManager = LinearLayoutManager(requireActivity())
                        rv.adapter = adapter
                        boolReport = false
                    }
                } else {
                    Log.e(this.toString(), "processLogin: Success Null!")
                }
            }
            is ScreenState.Error -> {
                Log.e(this.toString(), "processLogin: Failed!")
            }
        }
    }
}