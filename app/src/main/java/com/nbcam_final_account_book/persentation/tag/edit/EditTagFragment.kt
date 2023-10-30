package com.nbcam_final_account_book.persentation.tag.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nbcam_final_account_book.databinding.EditTagFragmentBinding
import com.nbcam_final_account_book.persentation.tag.TagPage


class EditTagFragment : Fragment() {
	private var _binding: EditTagFragmentBinding? = null
	private val binding get() = _binding!!


	private val viewModel by lazy {
		ViewModelProvider(
			this@EditTagFragment
		)[EditTagViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = EditTagFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
	}

	private fun initView() = with(binding) {
		val args: EditTagFragmentArgs by navArgs()
		val page = args.pages
		val tagName: String? = args.tagName

		ivBack.setOnClickListener {
			findNavController().popBackStack()
		}

		btnTagSave.setOnClickListener {
			// 저장 버튼 클릭 시 바로 room 에 저장 되는 로직 구현
		}

		category(page, tagName)
	}

	private fun initViewModel() = with(viewModel) {
		liveDummyTagInEditTag.observe(viewLifecycleOwner) {

		}
	}

	private fun category(page: TagPage, tagName: String?) = when (page) {

		TagPage.NEW -> {
			binding.tvPageTitle.text = "태그 생성"

			val result = binding.edtTagTitle.toString()
		}

		TagPage.MODIFY -> {
			binding.tvPageTitle.text = "태그 수정"
			binding.edtTagTitle.setText(tagName)
		}
	}
}