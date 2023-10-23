package com.nbcam_final_account_book.unit

import java.util.concurrent.atomic.AtomicLong

object Unit {

    // EntryModel의 input type
    const val INPUT_TYPE_INCOME = "input_type_income"
    const val INPUT_TYPE_PAY = "input_type_pay"

    // Firebase 저장 경로
    const val PATH_ENTRY = "path_entry"
    const val PATH_TAG = "path_tag"
    const val PATH_BUDGET = "path_budget"

    //id 부여

    private val budgetSetId = AtomicLong(0)
    fun setIdBudget():Long{
        return budgetSetId.getAndIncrement()
    }

}