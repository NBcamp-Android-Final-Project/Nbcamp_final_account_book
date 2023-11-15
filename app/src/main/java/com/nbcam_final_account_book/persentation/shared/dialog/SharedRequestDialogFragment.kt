package com.nbcam_final_account_book.persentation.shared.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nbcam_final_account_book.R

class SharedRequestDialogFragment : DialogFragment() {

    private lateinit var listener: SharedRequestListener

    interface SharedRequestListener {
        fun onAccept()
        fun onReject()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.EditNameAlertDialogStyle)
            val person = "○○○"
            val template = "△△△"

            builder.setTitle("가계부 공유 신청")
                .setMessage("${person}님이 ${template}를 공유하려 합니다.")
                .setPositiveButton("수락") { _, _ ->
                    listener.onAccept()
                    dismiss()
                }
                .setNegativeButton("거절") { _, _ ->
                    listener.onReject()
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}