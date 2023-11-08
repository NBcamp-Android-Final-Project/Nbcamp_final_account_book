package com.nbcam_final_account_book.persentation.template.addtemplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateAddFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstActivity
import com.nbcam_final_account_book.persentation.template.TemplateType
import com.nbcam_final_account_book.persentation.template.TemplateViewModel
import java.util.regex.Pattern


class TemplateAddFragment : Fragment() {

    private var _binding: TemplateAddFragmentBinding? = null
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private val binding get() = _binding!!

    private val viewModel: TemplateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = TemplateAddFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        // 키보드를 나타내는 Handler를 추가합니다.
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded) { // 프래그먼트가 여전히 추가된 상태인지 확인합니다.
                binding.templateEdtInput.requestFocus()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.templateEdtInput, InputMethodManager.SHOW_IMPLICIT)
            }
        }, 100) // 100밀리초 후에 실행
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        //백버튼 콜백 제어
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackEvent()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        //백버튼 콜백 해제
        onBackPressedCallback.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 키보드를 숨깁니다.
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.templateEdtInput.windowToken, 0)

        _binding = null
    }

    private fun initView() = with(binding) {

//        templateEdtInput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }

//            override fun afterTextChanged(s: Editable?) {
//
//                val text = s.toString()
//                if (text == "") {
//                    templateEdtInput.setTextColor(Color.BLUE)
//                    templateEdtInput.text = "회원님만의 가계부 이름을 만들어 주세요."
//                } else if (isEnable(text)) {
//                    templateEdtInput.setTextColor(Color.RED)
//                    templateEdtInput.text = "불가능한 문자가 포함되어있습니다 : '#' , '[' , ']' , '$' , '.' "
//                } else if (!isEnable(text)) {
//                    templateEdtInput.setTextColor(Color.BLUE)
//                    templateEdtInput.text = "가능한 형식 입니다"
//                }
//            }

//        })

        templateAddBtnOk.setOnClickListener {

            val templateTitle = templateEdtInput.text.toString()

            if (templateTitle.isNotEmpty()) {
                if (isEnable(templateTitle)) {
                    Toast.makeText(
                        requireContext(),
                        "불가능한 문자가 포함되어 있습니다 : '#' , '[' , ']' , '\$' , '.'",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    updateTitle(title = templateTitle)
                    findNavController().navigate(R.id.action_templateAddFragment_to_templateBudgetFragment)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "제목을 입력해주세요.",
                    Toast.LENGTH_LONG
                )
                    .show()

            }


        }
    }

    private fun onBackEvent() = with(viewModel) {
        val type = type() ?: return

        if (type == TemplateType.NEW) {
            val intent = Intent(requireContext(), FirstActivity::class.java)
            logout()
            startActivity(intent)
            requireActivity().finish()
        } else {
            requireActivity().finish()
        }
    }

    private fun updateTitle(title: String) {
        viewModel.updateLiveTitle(title)
    }

    private fun isEnable(input: String): Boolean {
        val pattern = Pattern.compile("[#\\[\\].$]")

        return pattern.matcher(input).find()
    }


}