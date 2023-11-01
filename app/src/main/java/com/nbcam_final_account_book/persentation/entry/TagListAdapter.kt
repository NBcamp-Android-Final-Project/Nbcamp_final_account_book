package com.nbcam_final_account_book.persentation.entry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.databinding.ItemTagBinding

class TagListAdapter(private val onItemClick: (TagEntity) -> Unit) :
    ListAdapter<TagEntity, TagListAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<TagEntity>() {
        override fun areItemsTheSame(oldItem: TagEntity, newItem: TagEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TagEntity, newItem: TagEntity): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemTagBinding,
        private val onItemClick: (TagEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: TagEntity) = with(binding) {
//            ivTagIcon.setImageResource(item.img)
            tvTagTitle.text = item.title

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}