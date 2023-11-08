package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.nbcam_final_account_book.R

class MyPageLogoutDialog(context: Context, onLogoutConfirmed: () -> Unit) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context, R.style.EditNameAlertDialogStyle)
        builder.apply {
            setTitle("DuToom")
            setMessage("로그아웃 하시겠습니까?")
            setPositiveButton("확인") { _, _ ->
                onLogoutConfirmed()
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
        }

        dialog = builder.create()
        dialog.show()
    }
}