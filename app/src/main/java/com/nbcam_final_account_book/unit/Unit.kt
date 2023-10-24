package com.nbcam_final_account_book.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicLong

object Unit {

    // EntryModel의 input type
    const val INPUT_TYPE_INCOME = "input_type_income"
    const val INPUT_TYPE_PAY = "input_type_pay"

    // Firebase 저장 경로
    const val TEMPLATE_LIST = "template_list"
    const val TEMPLATE_DATA = "template_data"

    //id 부여

    private val budgetSetId = AtomicLong(0)
    fun setIdBudget():Long{
        return budgetSetId.getAndIncrement()
    }
    val liveKey: MutableLiveData<String> get() = MutableLiveData()

}