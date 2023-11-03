package com.nbcam_final_account_book.persentation.chart

import androidx.compose.ui.graphics.Color

data class ChartFilterModel(
    val name: String,
    val amount: Double,
    val color: Color,
    val day: String,
)
