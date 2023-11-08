package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.nbcam_final_account_book.R

class MyPageWithdrawDialog(context: Context, onWithdrawConfirmed: () -> Unit) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context, R.style.EditNameAlertDialogStyle)
        val customView = View.inflate(context, R.layout.my_page_withdraw_dialog, null)

        builder.apply {
            setTitle("DuToom")
            setView(customView)
            setPositiveButton("확인") { _, _ ->
                onWithdrawConfirmed()
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
        }

        dialog = builder.create()
        dialog.show()
    }
}