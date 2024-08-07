package com.nbcam_final_account_book.presentation.template.addbudget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.TemplateBudgetFragmentBinding
import com.nbcam_final_account_book.presentation.main.MainActivity
import com.nbcam_final_account_book.presentation.template.TemplateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.text.DecimalFormat

class TemplateBudgetFragment : Fragment() {

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }

    private var _binding: TemplateBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private val viewModel: TemplateBudgetViewModel by lazy {
        ViewModelProvider(
            this@TemplateBudgetFragment,
            TemplateBudgetViewModelFactory(requireContext())
        )[TemplateBudgetViewModel::class.java]
    }
    private val sharedViewModel: TemplateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TemplateBudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //백버튼 콜백 제어
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_templateBudgetFragment_to_templateAddFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        //백버튼 콜백 해제
        onBackPressedCallback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {

        templateEdtInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isNotEmpty()) {

                    // 입력된 문자열을 숫자로 파싱
                    val number = text.replace(",", "").toLong()

                    // 숫자를 천 단위로 쉼표로 구분하여 포맷
                    val decimalFormat = DecimalFormat("#,###")
                    val formattedText = decimalFormat.format(number)

                    // EditText에 포맷된 숫자를 설정
                    templateEdtInput.removeTextChangedListener(this) // 무한 루프 방지를 위해 TextWatcher 제거
                    templateEdtInput.setText(formattedText)
                    templateEdtInput.setSelection(formattedText.length) // 커서 위치를 맨 뒤로 이동
                    templateEdtInput.addTextChangedListener(this) //

                }

            }
        })

        templateBudgetBtnOk.setOnClickListener {
            val text = templateEdtInput.text.toString()
            val number: Long = try {
                if (text.isNotEmpty()) {
                    text.replace(",", "").toLong()
                } else {
                    0
                }
            } catch (e: NumberFormatException) {
                0
            }

            CoroutineScope(Dispatchers.Main).launch {
                val model = insertFirstTemplate(number.toString())

                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra(EXTRA_RESULT, model)
                }
                Log.d("인텐트", model.toString())
                startActivity(intent)
                requireActivity().finish()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun insertFirstTemplate(budget: String): TemplateEntity {
        val title = sharedViewModel.getCurrentTitle()
        return viewModel.insertTemplate(title, budget) // 무조건 먼저 실행 되어 룸에 삽입 되어야 함
    }


}