package com.nbcam_final_account_book.unit

import android.os.Parcelable
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.persentation.budget.BudgetModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReturnSettingModel(
    val templateEntity: TemplateEntity,
    val budgetModel: BudgetModel
) : Parcelable