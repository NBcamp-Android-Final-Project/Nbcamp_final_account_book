package com.nbcam_final_account_book.persentation.entry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemEntrySwipeBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

	private val string = arrayOf("지출", "수입")

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
		val view = ItemEntrySwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
		holder.onBind(string[position])
	}

	override fun getItemCount(): Int = 2

	inner class ViewHolder(binding: ItemEntrySwipeBinding) : RecyclerView.ViewHolder(binding.root) {

		private val title = binding.tvTitle
//		private val minus = binding.icMinus

		fun onBind(item: String) {
			title.text = item
//			if (itemCount == 2) minus.visibility = View.GONE
		}
	}
}