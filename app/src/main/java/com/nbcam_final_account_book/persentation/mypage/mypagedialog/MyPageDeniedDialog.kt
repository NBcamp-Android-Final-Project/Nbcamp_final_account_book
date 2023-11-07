package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.nbcam_final_account_book.R

class MyPageDeniedDialog(context: Context) {
    private val dialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context, R.style.EditNameAlertDialogStyle)
        builder.apply {
            setMessage("갤러리를 열려면 권한이 필요합니다. 권한을 설정하시겠습니까?")
            setPositiveButton("설정") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
        }

        dialog = builder.create()
        dialog.show()
    }
}