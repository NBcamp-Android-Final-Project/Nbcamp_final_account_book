package com.nbcam_final_account_book.persentation.entry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ItemTagBinding
import com.nbcam_final_account_book.persentation.tag.TagModel

class TagListAdapter(private val onItemClick: (TagModel) -> Unit) :
	ListAdapter<TagModel, TagListAdapter.ViewHolder>(object :
		DiffUtil.ItemCallback<TagModel>() {
		override fun areItemsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: TagModel, newItem: TagModel): Boolean {
			return oldItem == newItem
		}
	}) {
//	private val selectedItems = arrayOf(1)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.onBind(getItem(position))
	}

	inner class ViewHolder(private val binding: ItemTagBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun onBind(item: TagModel) = with(binding) {
			ivTagIcon.setImageResource(item.img)
			tvTagTitle.text = item.tagName

			itemView.setOnClickListener {
				onItemClick(item)
//				selectItem(binding, item)
			}
		}
	}

//	private fun selectItem(binding: ItemTagBinding, tag: Tag) {
//		if (selectedItems.isEmpty()) {
//			selectedItems.plus(tag.icon)
//		} else {
//			binding.ivIsChecked.visibility = View.VISIBLE
//		}
//	}
//
//	private fun changeBackgroundColor(binding: ItemTagBinding, resId: Int) {
//		binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, resId))
//	}
}