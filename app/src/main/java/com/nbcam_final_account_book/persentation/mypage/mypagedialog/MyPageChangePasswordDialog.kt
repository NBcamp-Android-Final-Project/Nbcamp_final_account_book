package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageChangePasswordDialogBinding

class MyPageChangePasswordDialog : DialogFragment() {

    private var _binding: MyPageChangePasswordDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var alertDialog: AlertDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyPageChangePasswordDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = MyPageChangePasswordDialogBinding.inflate(layoutInflater, null, false)
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        val newTextWatcher = createTextWatcher(newPasswordEv, newPasswordInputLayout)
        val confirmTextWatcher = createTextWatcher(confirmPasswordEv, confirmPasswordInputLayout)

        alertDialog = AlertDialog.Builder(mContext, R.style.EditNameAlertDialogStyle)
            .setTitle("비밀번호 변경")
            .setView(root)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장", null)
            .create().apply {
                setCancelable(false)
            }

        tvDialogCancel.setOnClickListener {
            dismiss()
        }

        tvDialogSave.isEnabled = false
        tvDialogSave.setOnClickListener {
            // 저장 버튼 클릭 시 수행할 작업을 여기에 추가
            val currentPassword = currentPasswordEv.text.toString()
            val newPassword = newPasswordEv.text.toString()
            val confirmPassword = confirmPasswordEv.text.toString()
            reauthenticateAndChangePassword(currentPassword, newPassword, confirmPassword)
        }

        newPasswordEv.addTextChangedListener(newTextWatcher)
        confirmPasswordEv.addTextChangedListener(confirmTextWatcher)
    }

    private fun reauthenticateAndChangePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(currentUser?.email!!, currentPassword)

        currentUser.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    if (newPassword == confirmPassword) {
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    mContext.showToast("비밀번호가 변경되었습니다.")
                                    dismiss()
                                } else {
                                    mContext.showToast("비밀번호 변경에 실패했습니다.")
                                }
                            }
                    } else {
                        mContext.showToast("새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.")
                    }
                } else {
                    mContext.showToast("현재 비밀번호가 올바르지 않습니다.")
                }
            }
    }

    private fun createTextWatcher(
        editText: TextInputEditText,
        inputLayout: TextInputLayout
    ): TextWatcher = with(binding) {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newPassword = newPasswordEv.text.toString()
                val confirmPassword = confirmPasswordEv.text.toString()

                val isNewPasswordValid = isPasswordValid(newPassword)
                val isNewPasswordConfirmed = newPassword == confirmPassword

                inputLayout.error = when (editText) {
                    newPasswordEv -> if (isNewPasswordValid || newPassword.isEmpty()) "" else "비밀번호는 알파벳, 숫자, 특수문자를 혼합하여 8~20자로 입력해주세요."
                    confirmPasswordEv -> if (isNewPasswordConfirmed || confirmPassword.isEmpty()) "" else "비밀번호가 일치하지 않습니다."
                    else -> ""
                }

                val isFormValid = isNewPasswordValid && isNewPasswordConfirmed
                tvDialogSave.isEnabled = isFormValid
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,20}\$")
        return passwordRegex.matches(password)
    }

    private fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}