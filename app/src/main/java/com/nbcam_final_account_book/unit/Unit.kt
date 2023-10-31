package com.nbcam_final_account_book.unit

import java.util.concurrent.atomic.AtomicLong

object Unit {

    // EntryModel의 input type
    const val INPUT_TYPE_INCOME = "input_type_income"
    const val INPUT_TYPE_PAY = "input_type_pay"

    // Firebase 저장 경로
    const val TEMPLATE_LIST = "template_list"
    const val TEMPLATE_DATA = "template_data"


    val tagSetId = AtomicLong(0)
    fun setIdTag():Long{
        return tagSetId.getAndIncrement()
    }

    //Todo owner를 android studio에서 고려

}