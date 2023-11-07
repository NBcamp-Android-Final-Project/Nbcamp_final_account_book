package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R

class MyPageChangePasswordDialog(context: Context) {

    private val alertDialog: AlertDialog
    private val mContext: Context
    private val currentPasswordInputLayout: TextInputLayout
    private val currentPasswordEditText: TextInputEditText
    private val newPasswordInputLayout: TextInputLayout
    private val newPasswordEditText: TextInputEditText
    private val confirmPasswordInputLayout: TextInputLayout
    private val confirmPasswordEditText: TextInputEditText

    init {
        mContext = context

        val dialogView = View.inflate(context, R.layout.my_page_change_password_dialog, null)
        currentPasswordInputLayout = dialogView.findViewById(R.id.current_password_input_layout)
        currentPasswordEditText = currentPasswordInputLayout.findViewById(R.id.current_password_ev)
        newPasswordInputLayout = dialogView.findViewById(R.id.new_password_input_layout1)
        newPasswordEditText = newPasswordInputLayout.findViewById(R.id.new_password_ev1)
        confirmPasswordInputLayout = dialogView.findViewById(R.id.confirm_password_input_layout)
        confirmPasswordEditText = confirmPasswordInputLayout.findViewById(R.id.confirm_password_ev)

        val newTextWatcher = createTextWatcher(newPasswordEditText, newPasswordInputLayout)
        val confirmTextWatcher = createTextWatcher(confirmPasswordEditText, confirmPasswordInputLayout)

        alertDialog = AlertDialog.Builder(context, R.style.EditNameAlertDialogStyle)
            .setTitle("비밀번호 변경")
            .setView(dialogView)
            .setPositiveButton("저장") { _, _ ->
                val currentPassword = currentPasswordEditText.text.toString()
                val newPassword = confirmPasswordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()
                reauthenticateAndChangePassword(currentPassword, newPassword, confirmPassword)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        newPasswordEditText.addTextChangedListener(newTextWatcher)
        confirmPasswordEditText.addTextChangedListener(confirmTextWatcher)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    private fun reauthenticateAndChangePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(currentUser!!.email!!, currentPassword)

        currentUser.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    if (newPassword == confirmPassword) {
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    Toast.makeText(mContext, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                                    alertDialog.dismiss()
                                } else {
                                    Toast.makeText(mContext, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(mContext, "새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(mContext, "현재 비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createTextWatcher(
        editText: TextInputEditText,
        inputLayout: TextInputLayout
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newPassword = newPasswordEditText.text.toString()
                val confirmPassword = confirmPasswordEditText.text.toString()

                val isNewPasswordValid = isPasswordValid(newPassword)
                val isNewPasswordConfirmed = newPassword == confirmPassword

                inputLayout.error = when (editText) {
                    newPasswordEditText -> if (isNewPasswordValid || newPassword.isEmpty()) "" else "비밀번호는 알파벳, 숫자, 특수문자를 혼합하여 8~20자로 입력해주세요."
                    confirmPasswordEditText -> if (isNewPasswordConfirmed || confirmPassword.isEmpty()) "" else "비밀번호가 일치하지 않습니다."
                    else -> ""
                }

                val isFormValid = isNewPasswordValid && isNewPasswordConfirmed
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = isFormValid
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,20}\$")
        return passwordRegex.matches(password)
    }
}