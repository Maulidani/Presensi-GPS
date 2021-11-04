package com.skripsi.presensigps.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.skripsi.presensigps.R
import com.skripsi.presensigps.adapter.ViewPagerAdapter

class ReportViewPagerFragment : Fragment() {

    private val viewPager: ViewPager2 by lazy { requireActivity().findViewById(R.id.viewPager) }
    private val tabLayout: TabLayout by lazy { requireActivity().findViewById(R.id.tabLayout) }
    private val context = "report"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager, lifecycle, context
        )
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Hari ini"
                1 -> tab.text = "Semua"
            }
        }.attach()
    }
}