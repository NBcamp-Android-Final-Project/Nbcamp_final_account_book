package com.nbcam_final_account_book.util

import android.content.Context
import android.widget.Toast

object ContextUtil {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}