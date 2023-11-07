package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nbcam_final_account_book.R

class MyPageEditNameDialog(context: Context, currentName: String, private val onNameUpdated: (String) -> Unit) {

    private val alertDialog: AlertDialog

    init {
        val dialogView = View.inflate(context, R.layout.my_page_edit_name_dialog, null)
        val editNameInputLayout = dialogView.findViewById<TextInputLayout>(R.id.edit_name_input_layout)
        val editNameEditText = editNameInputLayout.findViewById<TextInputEditText>(R.id.edit_name_ev)
        val textWatcher = nameTextWatcher(editNameInputLayout)

        editNameEditText.setText(currentName)

        alertDialog = AlertDialog.Builder(context, R.style.EditNameAlertDialogStyle)
            .setTitle("이름 수정")
            .setView(dialogView)
            .setPositiveButton("저장") { _, _ ->
                val newName = editNameEditText.text.toString()
                onNameUpdated(newName)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        editNameEditText.addTextChangedListener(textWatcher)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    private fun nameTextWatcher(editNameInputLayout: TextInputLayout) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newName = s.toString()
                val isValid = isNameValid(newName)

                if (isValid) {
                    editNameInputLayout.error = null
                } else {
                    editNameInputLayout.error = "1~10자 이내로 띄어쓰기 없이 입력하세요."
                }

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isValid
            }
        }

    private fun isNameValid(name: String): Boolean {
        val nameRegex = Regex("^[a-zA-Z가-힣\\d!@#\$%^&*]{1,10}\$")
        return nameRegex.matches(name) && !name.contains(" ")
    }
}