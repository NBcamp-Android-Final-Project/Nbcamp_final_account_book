package com.nbcam_final_account_book.persentation.mypage.mypagedialog

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageEditNameDialogBinding
import com.nbcam_final_account_book.util.ContextUtil.showToast

class MyPageEditNameDialog(
    private val currentName: String,
    private val onNameUpdated: (String) -> Unit
) : DialogFragment() {

    private var _binding: MyPageEditNameDialogBinding? = null
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
        _binding = MyPageEditNameDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = MyPageEditNameDialogBinding.inflate(layoutInflater, null, false)
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
        editNameEv.setText(currentName)
        val textWatcher = nameTextWatcher(editNameInputLayout)

        alertDialog = AlertDialog.Builder(mContext, R.style.EditNameAlertDialogStyle)
            .setTitle("이름 수정")
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
            val newName = editNameEv.text.toString()
            onNameUpdated(newName)
            showToast(mContext, "이름을 수정하였습니다.")
            dismiss()
        }

        editNameEv.addTextChangedListener(textWatcher)
    }

    private fun nameTextWatcher(editNameInputLayout: TextInputLayout) =
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newName = s.toString()
                val isValid = isNameValid(newName)

                editNameInputLayout.error = if (isValid) "" else "1~10자 이내로 띄어쓰기 없이 입력하세요."
                binding.tvDialogSave.isEnabled = isValid
            }
        }

    private fun isNameValid(name: String): Boolean {
        val nameRegex = Regex("^[a-zA-Z가-힣\\d!@#\$%^&*]{1,10}\$")
        return nameRegex.matches(name) && !name.contains(" ")
    }
}