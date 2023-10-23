package com.nbcam_final_account_book.persentation.lock.pin

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.databinding.PinFragmentBinding
import com.nbcam_final_account_book.persentation.lock.LockSharedViewModel
import com.nbcam_final_account_book.persentation.lock.LockViewModelFactory

class PinFragment : Fragment() {

    companion object {
        const val PIN = "PinFragment"
    }

    private var _binding: PinFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: LockSharedViewModel
    private lateinit var viewModel: PinViewModel
    private lateinit var navController: NavController

    private lateinit var tvAlert: TextView
    private lateinit var ivLine: Array<ImageView>
    private lateinit var numberButtons: Array<AppCompatButton>
    private lateinit var btnDelete: AppCompatImageButton

    private var pin1 = ""
    private var pin2 = ""
    private var currentLine = 0
    private var isSecondInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PinFragment", "현재 위치: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("PinFragment", "현재 위치: onCreateView")
        _binding = PinFragmentBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(this, LockViewModelFactory(SharedProviderImpl(requireContext())))[LockSharedViewModel::class.java]
        viewModel = ViewModelProvider(this, PinViewModelFactory(SharedProviderImpl(requireContext())))[PinViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PinFragment", "현재 위치: onViewCreated")
        navController = findNavController()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PinFragment", "현재 위치: onDestroy")
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("PinFragment", "현재 위치: onDestroyView")
    }


    private fun initView() = with(binding) {
        tvAlert = pinTvAlert
        tvAlert.text = "가계부 비밀번호를 입력해주세요."

        ivLine = arrayOf(
            pinIvLine1, pinIvLine2, pinIvLine3, pinIvLine4
        )
        numberButtons = arrayOf(
            pinNumpad.btn1,
            pinNumpad.btn2,
            pinNumpad.btn3,
            pinNumpad.btn4,
            pinNumpad.btn5,
            pinNumpad.btn6,
            pinNumpad.btn7,
            pinNumpad.btn8,
            pinNumpad.btn9,
            pinNumpad.btn0
        )
        btnDelete = pinNumpad.btnDelete

        setNumberButtonListeners()
        setDeleteButtonListener()
    }

    private fun setDeleteButtonListener() {
        btnDelete.setOnClickListener {
            if (isSecondInput && pin2.isNotEmpty() && pin2.length <= 4) {
                pin2 = pin2.dropLast(1)
                ivLine[currentLine - 1].setImageResource(R.drawable.ic_line)
                currentLine--
            } else if (!isSecondInput && pin1.isNotEmpty() && pin1.length <= 4) {
                pin1 = pin1.dropLast(1)
                ivLine[currentLine - 1].setImageResource(R.drawable.ic_line)
                currentLine--
            }
        }
    }

    private fun setNumberButtonListeners() {
        for (btn in numberButtons) {
            btn.setOnClickListener {
                val num = btn.text.toString()
                if (!isSecondInput && pin1.length < 4) { // 첫 번째 입력
                    pin1 += num
                    ivLine[currentLine].setImageResource(R.drawable.ic_circle)
                    currentLine++

                    if (pin1.length == 4) {
                        Log.d(PIN, "First Input: $pin1")
                        resetInput()
                        isSecondInput = true
                        tvAlert.text = "확인을 위해 한번 더 입력해주세요."
                    }
                } else if (isSecondInput && pin2.length < 4) {  // 두 번째 입력
                    pin2 += num
                    ivLine[currentLine].setImageResource(R.drawable.ic_circle)
                    currentLine++

                    if (pin2.length == 4) { // 두 번째 비밀번호가 4자리 입력되면
                        Log.d(PIN, "First / Second Input: $pin1 / $pin2")

                        if (viewModel.arePinMatching(pin1, pin2)) {
                            sharedViewModel.savePassword(pin2)
                            /*sharedViewModel.password.observe(viewLifecycleOwner) { password ->
                                Log.d("PinFragment", "Password: $password")
                            }*/
                            sharedViewModel.setIsPinSet(true)
                            /*sharedViewModel.isPinSet.observe(viewLifecycleOwner) { isPinSet ->
                                Log.d("PinFragment", "!!!!!!!isPinSet: $isPinSet")
                            }*/
                            navController.popBackStack(R.id.lockSettingFragment, false)
                        } else {
                            tvAlert.text = "비밀번호가 일치하지 않습니다.\n처음부터 다시 시도해주세요."
                            pin1 = ""
                            pin2 = ""
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