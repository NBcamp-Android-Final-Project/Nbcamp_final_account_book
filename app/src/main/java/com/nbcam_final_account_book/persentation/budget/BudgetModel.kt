package com.nbcam_final_account_book.persentation.budget

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BudgetModel(
    val id: Long = 0,
    val budget: String, // 예산값
) : Parcelable