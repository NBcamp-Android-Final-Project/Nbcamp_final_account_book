package com.nbcam_final_account_book.persentation.template.addbudget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateBudgetFragmentBinding
import com.nbcam_final_account_book.persentation.login.LoginActivity
import com.nbcam_final_account_book.persentation.main.MainActivity
import com.nbcam_final_account_book.persentation.template.TemplateViewModel
import com.nbcam_final_account_book.persentation.template.TemplateViewModelFactory
import java.lang.NumberFormatException
import java.text.DecimalFormat

class TemplateBudgetFragment : Fragment() {

    private var _binding: TemplateBudgetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            TemplateViewModelFactory(requireContext())
        )[TemplateViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TemplateBudgetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                findNavController().navigate(R.id.action_templateBudgetFragment_to_templateAddFragment)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {

        templateBudgetEdtInput.addTextChangedListener(object : TextWatcher {
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
                    templateBudgetEdtInput.removeTextChangedListener(this) // 무한 루프 방지를 위해 TextWatcher 제거
                    templateBudgetEdtInput.setText(formattedText)
                    templateBudgetEdtInput.setSelection(formattedText.length) // 커서 위치를 맨 뒤로 이동
                    templateBudgetEdtInput.addTextChangedListener(this) //

                }

            }
        })

        templateBudgetBtnOk.setOnClickListener {

            val number: Long = try {
                if (templateBudgetEdtInput.text.isNotEmpty()) {
                    templateBudgetEdtInput.text.toString().replace(",", "").toLong()
                } else {
                    0
                }
            } catch (e: NumberFormatException) {
                0
            }

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun insertFirstTemplate(title: String) = with(viewModel) {
        insertFirstTemplate(title)
    }

    private fun addFirstTemplateToFirebase() = with(viewModel) {
        addTemplateFirst()
    }


}