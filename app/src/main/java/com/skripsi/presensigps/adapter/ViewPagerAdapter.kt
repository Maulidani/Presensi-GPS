package com.skripsi.presensigps.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skripsi.presensigps.ui.fragment.AllHistoryFragment
import com.skripsi.presensigps.ui.fragment.PresenceFragment
import com.skripsi.presensigps.ui.fragment.ReportFragment
import com.skripsi.presensigps.ui.fragment.UserFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    context: String,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val _context = context

    private val total = 2

    override fun getItemCount(): Int = total

    override fun createFragment(position: Int): Fragment {

        return when (_context) {
            "presence" -> {
                return when (position) {
                    0 -> PresenceFragment("today")
                    1 -> PresenceFragment("all")
                    else -> Fragment()
                }
            }
            "report" -> {
                return when (position) {
                    0 -> ReportFragment("today")
                    1 -> ReportFragment("all")
                    else -> Fragment()
                }
            } "user" -> {
                return when (position) {
                    0 -> UserFragment("sales")
                    1 -> UserFragment("manager")
                    else -> Fragment()
                }
            } "all_history" -> {
                return when (position) {
                    0 -> AllHistoryFragment("presence")
                    1 -> AllHistoryFragment("report")
                    else -> Fragment()
                }
            }
            else -> {
                Fragment()
            }
        }
    }
}