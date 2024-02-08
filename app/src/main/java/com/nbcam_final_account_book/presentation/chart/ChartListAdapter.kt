package com.nbcam_final_account_book.presentation.chart

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.databinding.ChartTabListItemBinding

class ChartListAdapter : RecyclerView.Adapter<ChartListAdapter.ChartListViewHolder>() {
    private var chartList = listOf<ChartItem>()

    fun setItems(chartList: List<ChartItem>) {
        this.chartList = chartList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartListViewHolder {
        val binding = ChartTabListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartListViewHolder, position: Int) {
        val chartItem = chartList[position]
        holder.bind(chartItem)
    }

    override fun getItemCount(): Int = chartList.size

    class ChartListViewHolder(private val binding: ChartTabListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChartItem) {
            binding.chartTabPercTxt.text = "${item.percentage}%"
            binding.chartTabTagTxt.text = item.name
            binding.chartTabPriceTxt.text = NumberFormat.getNumberInstance().format(item.amount) + "원"
        }
    }
}

data class ChartItem(val name: String, val amount: Int, val percentage: Int)
