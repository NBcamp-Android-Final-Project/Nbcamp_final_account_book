package com.nbcam_final_account_book.persentation.entry

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity) {

	private val fragments = arrayOf(FirstFragment(), SecondFragment())

	override fun getItemCount(): Int {
		return fragments.size
	}

	override fun createFragment(position: Int): Fragment {
		return fragments[position]
	}
}