package com.nbcam_final_account_book.presentation.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nbcam_final_account_book.data.model.local.UserDataEntity
import com.nbcam_final_account_book.databinding.SharedItemBinding

class UserListAdapter(
    private val onItemClicked: (UserDataEntity) -> Unit
) : ListAdapter<UserDataEntity, UserListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<UserDataEntity>() {
        override fun areItemsTheSame(oldItem: UserDataEntity, newItem: UserDataEntity): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: UserDataEntity, newItem: UserDataEntity): Boolean {
            return oldItem == newItem
        }

    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SharedItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )//ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    class ViewHolder(private val binding: SharedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserDataEntity) = with(binding) {
            if (item.img.isNotEmpty()){
                sharedItemIvProfile.load(item.img) {
                    transformations(CircleCropTransformation())
                }
            }
            sharedItemName.text = item.name
            sharedItemEmail.text = item.id
        }
    }
}