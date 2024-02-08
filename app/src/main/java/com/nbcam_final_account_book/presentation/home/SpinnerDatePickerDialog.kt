package com.nbcam_final_account_book.presentation.home

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class SpinnerDatePickerDialog : DialogFragment() {

    private lateinit var listener: OnDateSetListener

    interface OnDateSetListener {
        fun onDateSet(year: Int, month: Int)
    }

    fun setOnDateSetListener(listener: HomeFragment) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.Theme_Holo_Light_Dialog_NoActionBar,  // Spinner 스타일의 테마
            { _, selectedYear, selectedMonth, _ ->
                listener.onDateSet(selectedYear, selectedMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            1
        )

        // Window의 배경을 투명하게 설정 (Spinner 스타일이 올바르게 표시되도록)
        datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return datePickerDialog
    }
}