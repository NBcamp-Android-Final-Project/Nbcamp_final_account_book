package com.nbcam_final_account_book.persentation.tag.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.databinding.EditTagFragmentBinding
import com.nbcam_final_account_book.persentation.main.MainViewModel
import com.nbcam_final_account_book.persentation.tag.TagPage


class EditTagFragment : Fragment() {
	private var _binding: EditTagFragmentBinding? = null
	private val binding get() = _binding!!

	private lateinit var key: String

	private val viewModel by lazy {
		ViewModelProvider(
			this@EditTagFragment,
			EditTagViewModelFactory(requireActivity())
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
		val tagName = args.tagName

		category(page, tagName)

		ivBack.setOnClickListener {
			findNavController().popBackStack()
		}

		btnTagSave.setOnClickListener {

			if (page == TagPage.NEW) {
				newTag()
			} else {
				tagModify(tagName = tagName)
			}

			findNavController().popBackStack()
		}
	}

	private fun category(page: TagPage, tagName: TagEntity?) = when (page) {

		TagPage.NEW -> {
			binding.tvPageTitle.text = "태그 생성"
		}

		TagPage.MODIFY -> {
			binding.tvPageTitle.text = "태그 수정"
			binding.edtTagTitle.setText(tagName!!.title)
		}
	}

	private fun newTag() {
		val result = binding.edtTagTitle.text.toString()
		viewModel.insertTag(
			TagEntity(
				id = 0,
				key = MainViewModel.liveKey.value.toString(),
				title = result
			)
		)
	}

	private fun tagModify(tagName: TagEntity?) {
		val result = binding.edtTagTitle.text.toString()
		viewModel.updateTag(tagName!!, result)
	}
}