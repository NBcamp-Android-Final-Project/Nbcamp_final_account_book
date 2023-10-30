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

		ivBack.setOnClickListener {
			findNavController().popBackStack()
		}

		val args: EditTagFragmentArgs by navArgs()
		val page = args.page
		category(page)
	}

	private fun initViewModel() = with(viewModel) {
		liveDummyTagInEditTag.observe(viewLifecycleOwner) {

		}
	}

	private fun category(page: TagPage) = when (page) {

		TagPage.NEW -> {
			binding.tvPageTitle.text = "태그 생성"

			val result = binding.edtTagTitle.toString()
		}

		TagPage.MODIFY -> {
			binding.tvPageTitle.text = "태그 수정"

			val result = binding.edtTagTitle.toString()

		}
	}
}