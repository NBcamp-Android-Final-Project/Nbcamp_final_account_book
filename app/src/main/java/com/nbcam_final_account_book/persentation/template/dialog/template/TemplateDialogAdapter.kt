package com.nbcam_final_account_book.persentation.template.dialog.template

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            ), onItemClick
            , onItemDeleteClick

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
                val textView = TextView(root.context)
                textView.text = "템플릿 삭제"
                textView.setPadding(70, 30, 20, 30)
                textView.textSize = 20f
                textView.setBackgroundColor(Color.WHITE)
                textView.setTextColor(Color.BLACK)

                val builder = AlertDialog.Builder(root.context)
                builder.setCustomTitle(textView)
                builder.setMessage("${item.templateTitle}을 삭제하시겠습니까??")
                builder.setNegativeButton("예") { _, _ ->

                    onItemDeleteClick(item)
                }
                builder.setPositiveButton("아니오") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

        }

    }
}