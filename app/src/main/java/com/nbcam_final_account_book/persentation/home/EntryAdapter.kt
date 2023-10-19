package com.nbcam_final_account_book.persentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.HomeBottomSheetItemBinding
import com.nbcam_final_account_book.persentation.entry.EntryModel

class EntryAdapter(private val entries: List<EntryModel>) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = HomeBottomSheetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount() = entries.size

    class EntryViewHolder(private val binding: HomeBottomSheetItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: EntryModel) {
            binding.tvCategory.text = entry.tag
            binding.tvTitle.text = entry.title
            binding.tvValue.text = entry.value
        }
    }
}
