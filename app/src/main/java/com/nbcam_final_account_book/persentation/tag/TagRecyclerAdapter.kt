package com.nbcam_final_account_book.persentation.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemCategoryBinding

class TagRecyclerAdapter : RecyclerView.Adapter<TagRecyclerAdapter.ViewHolder>() {

	private val items = arrayOf("지출", "수입", "용돈", "경조사", "비자금")
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): TagRecyclerAdapter.ViewHolder {
		val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: TagRecyclerAdapter.ViewHolder, position: Int) {
		holder.onBind(items[position])
	}

	override fun getItemCount(): Int {
		return items.size
	}

	inner class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
		private val title = binding.title

		fun onBind(item: String) {
			title.text = item
		}
	}
}