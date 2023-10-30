package com.nbcam_final_account_book.persentation.dialog.template

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nbcam_final_account_book.databinding.TemplateSelectDialogBinding
import com.nbcam_final_account_book.persentation.template.TemplateActivity

class TemplateDialogFragment() : DialogFragment() {

    private var _binding: TemplateSelectDialogBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        TemplateDialogAdapter()
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            TemplateDialogViewModelFactory(
                requireContext()
            )
        )[TemplateDialogViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TemplateSelectDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initView()

    }

    private fun initView() = with(binding) {


        templateBtnAdd.visibility = if (isOver3()) View.VISIBLE else View.INVISIBLE

        templateBtnAdd.setOnClickListener {
            toTemplateActivity()
        }

    }

    private fun initViewModel() {
        with(viewModel) {
            dialogLiveTemplateList.observe(viewLifecycleOwner) {
                if (it != null) {

                    adapter.submitList(it)

                } // if
            } // observe


        }
    }

    private fun isOver3(): Boolean { // 템플릿 리스트 사이즈가 3 미만이면 true 아니면 false
        return viewModel.getTemplateSizeInTemplateDialog()
    }

    private fun toTemplateActivity() {
        val intent = Intent(requireContext(), TemplateActivity::class.java)
        startActivity(intent)
    }

}