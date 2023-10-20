package com.nbcam_final_account_book.persentation.template.addtemplate

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TemplateAddFragmentBinding
import com.nbcam_final_account_book.persentation.template.TemplateViewModel
import com.nbcam_final_account_book.persentation.template.TemplateViewModelFactory
import java.util.regex.Pattern


class TemplateAddFragment : Fragment() {

    private var _binding: TemplateAddFragmentBinding? = null
    private val binding get() = _binding!!
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
        _binding = TemplateAddFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        templateAddEdtInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                val text = s.toString()
                if (text == "") {
                    templateAddTvError.setTextColor(Color.BLUE)
                    templateAddTvError.text = "제목을 지어주세요"
                } else if (isEnable(text)) {
                    templateAddTvError.setTextColor(Color.RED)
                    templateAddTvError.text = "불가능한 문자가 포함되어있습니다 : '#' , '[' , ']' , '$' , '.' "
                } else if (!isEnable(text)) {
                    templateAddTvError.setTextColor(Color.BLUE)
                    templateAddTvError.text = "가능한 형식 입니다"
                }
            }

        })

        templateAddBtnOk.setOnClickListener {

            val templateTitle = templateAddEdtInput.text.toString()

            if (isEnable(templateTitle)) {
                Toast.makeText(
                    requireContext(),
                    "금지된 문자가 포함되어 있습니다.",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {

                insertFirstTemplate(templateTitle) // room DB에 삽입
                addFirstTemplateToFirebase() // firebase에 삽입
                findNavController().navigate(R.id.action_templateAddFragment_to_templateBudgetFragment)

            }

        }
    }

    private fun initViewModel() = with(viewModel) {

    }


    private fun saveIsFirst(isFirst: Boolean) = with(viewModel) {
        saveIsFirst(isFirst)
    }

    private fun insertFirstTemplate(title: String) = with(viewModel) {
        insertFirstTemplate(title)
    }

    private fun addFirstTemplateToFirebase() = with(viewModel) {
        addTemplateFirst()
    }

    private fun isEnable(input: String): Boolean {
        val pattern = Pattern.compile("[#\\[\\].$]")

        return pattern.matcher(input).find()
    }
}