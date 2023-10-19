package com.nbcam_final_account_book.persentation.lock.locksetting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.databinding.LockSettingFragmentBinding

class LockSettingFragment : Fragment() {

    private var _binding: LockSettingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LockSettingViewModel

    private lateinit var tvAlert: TextView
    private lateinit var ivLine: Array<ImageView>
    private lateinit var numberButtons: Array<AppCompatButton>
    private lateinit var btnDelete: AppCompatImageButton

    private var password = ""
    private var currentLine = 0
    private lateinit var num: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LockSettingFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            LockSettingViewModelFactory(SharedProviderImpl(requireContext()))
        )[LockSettingViewModel::class.java]

        initView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        tvAlert = locksettingTvAlert
        tvAlert.text = "가계부 비밀번호를 입력해주세요."

        ivLine = arrayOf(
            locksettingIvLine1, locksettingIvLine2, locksettingIvLine3, locksettingIvLine4
        )
        numberButtons = arrayOf(
            locksettingNumpad.lockBtn1, locksettingNumpad.lockBtn2, locksettingNumpad.lockBtn3, locksettingNumpad.lockBtn4, locksettingNumpad.lockBtn5,
            locksettingNumpad.lockBtn6, locksettingNumpad.lockBtn7, locksettingNumpad.lockBtn8, locksettingNumpad.lockBtn9, locksettingNumpad.lockBtn0
        )
        btnDelete = locksettingNumpad.lockBtnDelete

        setNumberButtonListeners()
        setDeleteButtonListener()
    }

    private fun setDeleteButtonListener() {
        btnDelete.setOnClickListener {
            if (password.isNotEmpty() && password.length <= 4) {
                // 삭제 버튼을 누를 때 비밀번호 문자열에서 마지막 문자 제거
                password = password.dropLast(1)
                Log.d("DeletedPassword", "After: $password")

                // 해당 라인의 이미지 리소스를 원래대로 되돌림 (ic_line)
                ivLine[currentLine - 1].setImageResource(R.drawable.ic_line)
                currentLine--
            }
        }
    }

    private fun setNumberButtonListeners() {
        for (btn in numberButtons) {
            btn.setOnClickListener {
                num = btn.text.toString()
                if (password.length < 4) {
                    password += num
                    ivLine[password.length - 1].setImageResource(R.drawable.ic_circle)
                    currentLine++
                    Log.d("KeyPadClick", "Clicked: $num")
                }
            }
        }
    }

    private fun resetInput() {
        password = ""
        currentLine = 0

        for (i in 0 until 4) {
            ivLine[i].setImageResource(R.drawable.ic_line)
        }
    }
}