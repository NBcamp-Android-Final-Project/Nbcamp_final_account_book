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

    companion object {
        const val LOCK_SETTING = "LockSettingFragment"
    }

    private var _binding: LockSettingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LockSettingViewModel

    private lateinit var tvAlert: TextView
    private lateinit var ivLine: Array<ImageView>
    private lateinit var numberButtons: Array<AppCompatButton>
    private lateinit var btnDelete: AppCompatImageButton

    private var password1 = ""
    private var password2 = ""
    private var currentLine = 0
    private var isSecondInput = false

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
            locksettingNumpad.lockBtn1,
            locksettingNumpad.lockBtn2,
            locksettingNumpad.lockBtn3,
            locksettingNumpad.lockBtn4,
            locksettingNumpad.lockBtn5,
            locksettingNumpad.lockBtn6,
            locksettingNumpad.lockBtn7,
            locksettingNumpad.lockBtn8,
            locksettingNumpad.lockBtn9,
            locksettingNumpad.lockBtn0
        )
        btnDelete = locksettingNumpad.lockBtnDelete

        setNumberButtonListeners()
        setDeleteButtonListener()
    }

    private fun setDeleteButtonListener() {
        btnDelete.setOnClickListener {
            if (isSecondInput && password2.isNotEmpty() && password2.length <= 4) {
                password2 = password2.dropLast(1)
                ivLine[currentLine - 1].setImageResource(R.drawable.ic_line)
                currentLine--
            } else if (!isSecondInput && password1.isNotEmpty() && password1.length <= 4) {
                password1 = password1.dropLast(1)
                ivLine[currentLine - 1].setImageResource(R.drawable.ic_line)
                currentLine--
            }
        }
    }

    private fun setNumberButtonListeners() {
        for (btn in numberButtons) {
            btn.setOnClickListener {
                val num = btn.text.toString()
                if (!isSecondInput && password1.length < 4) { // 첫 번째 입력
                    password1 += num
                    ivLine[currentLine].setImageResource(R.drawable.ic_circle)
                    currentLine++

                    if (password1.length == 4) {
                        Log.d(LOCK_SETTING, "First Input: $password1")
                        resetInput()
                        isSecondInput = true
                        tvAlert.text = "확인을 위해 한번 더 입력해주세요."
                    }
                } else if (isSecondInput && password2.length < 4) {  // 두 번째 입력
                    password2 += num
                    ivLine[currentLine].setImageResource(R.drawable.ic_circle)
                    currentLine++

                    if (password2.length == 4) { // 두 번째 비밀번호가 4자리 입력되면
                        Log.d(LOCK_SETTING, "First / Second Input: $password1 / $password2")

                        if (viewModel.arePasswordMatching(password1, password2)) {
                            viewModel.savePassword(password1)
                            parentFragmentManager.beginTransaction().remove(this).commit()
                        } else {
                            tvAlert.text = "비밀번호가 일치하지 않습니다.\n처음부터 다시 시도해주세요."
                            password1 = ""
                            password2 = ""
                            resetInput()
                        }
                        isSecondInput = false
                    }
                }
            }
        }
    }

    private fun resetInput() {
        currentLine = 0
        for (i in 0 until 4) {
            ivLine[i].setImageResource(R.drawable.ic_line)
        }
    }
}