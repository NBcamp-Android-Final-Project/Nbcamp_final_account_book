package com.nbcam_final_account_book.persentation.mypage.mypagedialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageWithdrawDialogBinding

class MyPageWithdrawDialog(private val onWithdrawConfirmed: () -> Unit) : DialogFragment() {

    private var _binding: MyPageWithdrawDialogBinding? = null
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
        _binding = MyPageWithdrawDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = MyPageWithdrawDialogBinding.inflate(layoutInflater, null, false)
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

    private fun initView() = with(binding){
        alertDialog = AlertDialog.Builder(mContext, R.style.EditNameAlertDialogStyle)
            .setView(root)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장", null)
            .create().apply {
                setCancelable(false)
            }

        tvDialogCancel.setOnClickListener {
            dismiss()
        }

        tvDialogOkay.setOnClickListener {
            onWithdrawConfirmed()
            dismiss()
        }
    }
}