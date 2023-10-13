package com.nbcam_final_account_book.persentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nbcam_final_account_book.persentation.chart.ChartFragment
import com.nbcam_final_account_book.persentation.chat.ChatFragment
import com.nbcam_final_account_book.persentation.home.HomeFragment
import com.nbcam_final_account_book.persentation.more.MoreFragment

class MainViewpagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.apply {
            add(HomeFragment())
            add(ChartFragment())
            add(ChatFragment())
            add(MoreFragment())
        }
    }


    override fun getItemCount(): Int {
        return fragments.size

    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }



}


