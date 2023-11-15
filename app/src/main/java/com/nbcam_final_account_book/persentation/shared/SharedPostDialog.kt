package com.nbcam_final_account_book.persentation.shared

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nbcam_final_account_book.databinding.DialogSharedPostBinding

class SharedPostDialog : DialogFragment() {
    private var _binding: DialogSharedPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSharedPostBinding.inflate(inflater, container, false)
        binding.tvDialogCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogSharedPostBinding.inflate(layoutInflater, null, false)
        // Dialog 인스턴스 생성
        val dialog = Dialog(requireContext())
        // 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 타이틀 바 없애기
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 레이아웃 설정
        dialog.setContentView(binding.root)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        // 다이얼로그 크기 조정
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.apply {
            setLayout(width, height)
            // 다이얼로그 위치 조정
            val attributes = attributes
            attributes.gravity = Gravity.TOP

            // dp를 px로 변환
            val downDialogPx = (100 * resources.displayMetrics.density).toInt()
            // y 값을 설정하여 다이얼로그를 아래로 이동
            attributes.y = downDialogPx

            this.attributes = attributes
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}