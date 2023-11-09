package com.nbcam_final_account_book.persentation.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbcam_final_account_book.data.model.local.TagEntity
import com.nbcam_final_account_book.databinding.FragmentEntryModalBinding
import com.nbcam_final_account_book.persentation.tag.TagActivity

class EntryModalFragment : BottomSheetDialogFragment() {

	private var _binding: FragmentEntryModalBinding? = null
	private val binding get() = _binding!!
	private val viewModel: EntryViewModel by activityViewModels()

	private val tagListAdapter by lazy {
		TagListAdapter(onItemClick = { item ->
			onItemClickEvent(item)
		})
	}

	private fun onItemClickEvent(item: TagEntity) {
		viewModel.setCategory(item.title)
		viewModel.setCategoryDrawable(item.img)
		dismiss()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentEntryModalBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// 드래그 Disabled
		val dialog = dialog as BottomSheetDialog
		val bottomSheetBehavior = dialog.behavior
		bottomSheetBehavior.isDraggable = false

		initView()
		initViewModel()
	}

	private fun initView() = with(binding) {
		initRecyclerView()

		ivTagAdd.setOnClickListener {
			val intent = TagActivity.newIntent(requireActivity())
			startActivity(intent)
		}
	}

	private fun initViewModel() = with(viewModel) {

		liveTagList.observe(viewLifecycleOwner) {
			if (it != null) {
				tagListAdapter.submitList(it)
			}
		}
	}

	private fun initRecyclerView() {
		binding.rvTagContainer.adapter = tagListAdapter
	}
}