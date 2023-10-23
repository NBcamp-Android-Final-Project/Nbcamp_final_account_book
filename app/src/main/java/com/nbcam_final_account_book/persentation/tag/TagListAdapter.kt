package com.nbcam_final_account_book.persentation.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.ItemTagBinding

class TagListAdapter(private val onItemClick: (Int) -> Unit) :
	ListAdapter<Tag, TagListAdapter.ViewHolder>(object :
		DiffUtil.ItemCallback<Tag>() {
		override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
			return oldItem.title == newItem.title
		}

		override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
			return oldItem == newItem
		}

	}) {
	private val selectedItems = mutableListOf<Tag>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.onBind(getItem(position))
	}

	inner class ViewHolder(private val binding: ItemTagBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBind(item: Tag) = with(binding) {
			ivTagIcon.setImageResource(item.icon)
			tvTagTitle.text = item.title

			itemView.setOnClickListener {
				onItemClick(adapterPosition)
				selectItem(binding, item)
			}
		}
	}

	private fun selectItem(binding: ItemTagBinding, tag: Tag) {
		if (selectedItems.contains(tag)) {
			selectedItems.remove(tag)
			changeBackgroundColor(binding, R.color.calendar_sunday)
		} else {
			selectedItems.add(tag)
			changeBackgroundColor(binding, R.color.primary)
			binding.ivIsChecked.visibility = View.VISIBLE
		}
	}

	private fun changeBackgroundColor(binding: ItemTagBinding, resId: Int) {
		binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, resId))
	}
}