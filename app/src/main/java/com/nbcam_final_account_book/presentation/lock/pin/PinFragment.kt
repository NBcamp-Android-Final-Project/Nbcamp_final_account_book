package com.nbcam_final_account_book.presentation.lock.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.PinFragmentBinding
import com.nbcam_final_account_book.presentation.main.MainActivity
import com.nbcam_final_account_book.presentation.main.MainViewModel

class PinFragment : Fragment() {

    private var _binding: PinFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var ivCircle: Array<ImageView>
    private lateinit var numberButtons: Array<AppCompatButton>

    private var pin1 = ""
    private var pin2 = ""
    private var currentCircle = 0
    private var isSecondInput = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PinFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).toggleToolbar(false)
        (requireActivity() as MainActivity).toggleBottomNavi(false)
        navController = findNavController()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).toggleToolbar(true)
        (requireActivity() as MainActivity).toggleBottomNavi(true)
        _binding = null
    }

    private fun initView() = with(binding) {
        pinTvAlert.text = "가계부 비밀번호를 입력해주세요."
        ivCircle = arrayOf(
            pinIvCircle1, pinIvCircle2, pinIvCircle3, pinIvCircle4
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
            pinNumpad.btn0,
        )

        setNumberButtonListeners()
        setDeleteButtonListener()
        setCancelButonListener()

        /*if (sharedViewModel.isSwitch) {
            Toast.makeText(requireContext(), "비밀번호 변경", Toast.LENGTH_SHORT).show()
            // TODO: 기본 비밀번호를 입력 받고 난 후 새로운 비밀번호를 설정할 수 있도록 해야함
            setNumberButtonListeners()
            setDeleteButtonListener()
        } else {
            Toast.makeText(requireContext(), "비밀번호 새로운 설정", Toast.LENGTH_SHORT).show()
            setNumberButtonListeners()
            setDeleteButtonListener()
        }*/
    }

    private fun setCancelButonListener() = with(binding) {
        pinNumpad.btnCancel.setOnClickListener {
            navController.popBackStack(R.id.menu_mypage, false)
        }
    }

    private fun setDeleteButtonListener() = with(binding) {
        pinNumpad.btnDelete.setOnClickListener {
            if (isSecondInput && pin2.isNotEmpty() && pin2.length <= 4) {
                pin2 = pin2.dropLast(1)
                ivCircle[currentCircle - 1].setImageResource(R.drawable.ic_circle_inactive)
                currentCircle--
            } else if (!isSecondInput && pin1.isNotEmpty() && pin1.length <= 4) {
                pin1 = pin1.dropLast(1)
                ivCircle[currentCircle - 1].setImageResource(R.drawable.ic_circle_inactive)
                currentCircle--
            }
        }
    }

    private fun setNumberButtonListeners() = with(binding) {
        for (btn in numberButtons) {
            btn.setOnClickListener {
                val num = btn.text.toString()
                if (!isSecondInput && pin1.length < 4) { // 첫 번째 입력
                    pin1 += num
                    ivCircle[currentCircle].setImageResource(R.drawable.ic_circle_active)
                    currentCircle++

                    if (pin1.length == 4) {
                        resetInput()
                        isSecondInput = true
                        pinTvAlert.text = "확인을 위해 한번 더 입력해주세요."
                    }
                } else if (isSecondInput && pin2.length < 4) {  // 두 번째 입력
                    pin2 += num
                    ivCircle[currentCircle].setImageResource(R.drawable.ic_circle_active)
                    currentCircle++

                    if (pin2.length == 4) { // 두 번째 비밀번호가 4자리 입력되면
                        if (pin1 == pin2) {
                            sharedViewModel.saveSharedPrefPinNumber(pin2)
                            navController.popBackStack(R.id.menu_mypage, false)
                        } else {
                            pinTvAlert.text = "비밀번호가 일치하지 않습니다.\n처음부터 다시 시도해주세요."
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
        currentCircle = 0
        for (i in 0 until 4) {
            ivCircle[i].setImageResource(R.drawable.ic_circle_inactive)
        }
    }
}