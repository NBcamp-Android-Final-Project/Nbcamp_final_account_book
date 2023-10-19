package com.nbcam_final_account_book.persentation.lock.locksetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LockSettingFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, LockSettingViewModelFactory(SharedProviderImpl(requireContext())))[LockSettingViewModel::class.java]

        initView()
        return binding.root
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

//        setNumberButtonListeners()
//        setDeleteButtonListener()
    }

    private fun setDeleteButtonListener() {
        btnDelete.setOnClickListener {
            if (password.isNotEmpty()) {
                // 삭제 버튼을 누를 때 비밀번호 문자열에서 마지막 문자 제거
                password = password.dropLast(1)
                // 해당 라인의 이미지 리소스를 원래대로 되돌림 (ic_line)
                val lastFilledIndex = ivLine.indexOfFirst { it.tag == "filled" }
                if (lastFilledIndex >= 0) {
                    ivLine[lastFilledIndex].setImageResource(R.drawable.ic_line)
                    ivLine[lastFilledIndex].tag = "empty"
                }
            }
        }
    }

    private fun setNumberButtonListeners() {
        for (i in numberButtons.indices) {
            numberButtons[i].setOnClickListener {
                if (password.length < 4) { // 최대 4자리까지만 입력받음
                    password += i.toString()
                    ivLine[password.length - 1].setImageResource(R.drawable.ic_circle) // 입력한 라인 이미지를 변경 (ic_circle)
                    ivLine[password.length - 1].tag = "filled"
                }

                if (password.length == 4) {
                    if (viewModel.isFirstInput()) { // 첫 번째 입력인 경우
                        viewModel.setFirstInputPassword(password)
                        password = ""
                        Toast.makeText(requireContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                        // TODO: 사용자에게 다시 입력할 수 있도록 UI를 초기화하는 코드를 추가
                    } else {
                        // 두 번째 입력인 경우
                        if (viewModel.validateAndSavePassword(password)) {
                            // 비밀번호가 일치하는 경우
                            activity?.finish()
                        } else {
                            // 비밀번호가 일치하지 않는 경우
                            // TODO: 사용자에게 다시 입력할 수 있도록 UI를 초기화하는 코드를 추가
                            password = ""
                            Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // 4자리가 아니면 계속 입력 받음
                    // TODO: UI에서 비밀번호 입력 상태를 사용자에게 시각적으로 표시하는 코드를 추가
                }

                /*if (password.length == 4) {
                    // 4자리 비밀번호 입력이 완료되면 비밀번호 검증을 수행
                    if (viewModel.validateAndSavePassword(password)) {
                        // 비밀번호가 일치하는 경우
                        viewModel.setFirstInputPassword(password)
                        // TODO: LockSettingFragment를 닫고 LockActivity로 돌아갈 수 있는 코드를 추가
                        activity?.finish()
                    } else {
                        // 비밀번호가 일치하지 않는 경우
                        // TODO:사용자에게 알림을 표시하고 password를 초기화하여 다시 입력 가능하게 함
                        password = ""
                        Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }*/
            }
        }
    }
}