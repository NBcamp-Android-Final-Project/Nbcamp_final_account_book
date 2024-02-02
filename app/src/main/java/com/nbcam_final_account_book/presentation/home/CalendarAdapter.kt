package com.nbcam_final_account_book.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.CalendarItemDayBinding

class CalendarAdapter(private val context: Context, private val days: List<Day>) : BaseAdapter() {

    override fun getCount() = days.size

    override fun getItem(position: Int) = days[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = convertView?.let { CalendarItemDayBinding.bind(it) }
            ?: CalendarItemDayBinding.inflate(LayoutInflater.from(context), parent, false)

        val day = getItem(position)
        binding.tvDay.text = if (day.date == 0) "" else day.date.toString()


        binding.ivCircle.visibility = View.VISIBLE
        when (day.eventType) {
            EventType.INCOME -> binding.ivCircle.setBackgroundResource(R.drawable.circle_stroke_income)
            EventType.EXPEND -> binding.ivCircle.setBackgroundResource(R.drawable.circle_stroke_expend)
            EventType.MIX -> binding.ivCircle.setBackgroundResource(R.drawable.circle_stroke_mix)
            else -> binding.ivCircle.visibility = View.GONE
        }

        return binding.root
    }
}

enum class EventType {
    NONE, INCOME, EXPEND, MIX
}

data class Day(val date: Int, val eventType: EventType)
