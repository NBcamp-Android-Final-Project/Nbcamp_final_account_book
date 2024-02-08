package com.nbcam_final_account_book.presentation.template.dialog.template

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.TemplateSelectItemBinding


class TemplateDialogAdapter(
    private val onItemClick: (TemplateEntity) -> Unit,
    private val onItemDeleteClick: (TemplateEntity) -> Unit,

    ) : ListAdapter<TemplateEntity, TemplateDialogAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TemplateEntity>() {
        override fun areItemsTheSame(oldItem: TemplateEntity, newItem: TemplateEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TemplateEntity, newItem: TemplateEntity): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TemplateSelectItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), onItemClick, onItemDeleteClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: TemplateSelectItemBinding,
        private val onItemClick: (TemplateEntity) -> Unit,
        private val onItemDeleteClick: (TemplateEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TemplateEntity) = with(binding) {

            templateItemTvTitle.text = item.templateTitle

            itemView.setOnClickListener {
                onItemClick(item)
            }

            ivTemplateDelete.setOnClickListener {
                val builder = AlertDialog.Builder(root.context, R.style.EditNameAlertDialogStyle)
                builder.setTitle("템플릿 삭제")
                builder.setMessage("${item.templateTitle}을 삭제하시겠습니까??")
                builder.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setPositiveButton("확인") { _, _ ->
                    onItemDeleteClick(item)
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}