package com.nbcam_final_account_book.persentation.entry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemTagBinding
import com.nbcam_final_account_book.persentation.tag.Tag

class ModalTagListAdapter(private val onItemClick: (Int) -> Unit) :
	ListAdapter<Tag, ModalTagListAdapter.ViewHolder>(object :
		DiffUtil.ItemCallback<Tag>() {
		override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
			return oldItem.title == newItem.title
		}

		override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
			return oldItem == newItem
		}

	}) {
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
			}
		}
	}
}