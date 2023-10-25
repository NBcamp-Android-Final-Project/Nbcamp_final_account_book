package com.nbcam_final_account_book.persentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.data.model.local.EntryEntity
import com.nbcam_final_account_book.databinding.HomeBottomSheetItemBinding
import com.nbcam_final_account_book.persentation.entry.EntryModel

class EntryAdapter(var entries: List<EntryEntity>) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: HomeBottomSheetItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeBottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.binding.tvCategory.text = entry.tag
        holder.binding.tvTitle.text = entry.title
        holder.binding.tvValue.text = entry.value
    }

    override fun getItemCount(): Int = entries.size

    fun updateData(newEntries: List<EntryEntity>) {
        this.entries = newEntries
        notifyDataSetChanged()  // 알림을 보내 데이터가 변경되었음을 RecyclerView에 알림
    }
}
