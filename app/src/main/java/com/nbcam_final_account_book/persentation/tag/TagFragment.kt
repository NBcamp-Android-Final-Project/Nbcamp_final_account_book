package com.nbcam_final_account_book.persentation.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TagFragmentBinding


class TagFragment : Fragment() {

	private var _binding: TagFragmentBinding? = null
	private val binding get() = _binding!!

	private val viewModel by lazy {
		ViewModelProvider(
			this@TagFragment
		)[TagViewModel::class.java]
	}

	private val tagManageAdapter by lazy {
		TagManageAdapter(onItemClick = { position, item ->
			onItemClickEvent(position, item)
		})
	}

	private fun onItemClickEvent(position: Int, item: TagModel) {
		Toast.makeText(requireActivity(), "$position 번째 touch", Toast.LENGTH_SHORT).show()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = TagFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initView()
		initViewModel()
		initTag()
	}

	private fun initView() = with(binding) {

		ivBack.setOnClickListener {
			findNavController().popBackStack()
		}
	}

	private fun initViewModel() = with(viewModel) {
		liveDummyTagInTag.observe(viewLifecycleOwner) {

		}
	}

	private fun initTag() {
		// 임시 데이터
		val newList = mutableListOf<TagModel>()

		newList.apply {
			add(TagModel(0, R.drawable.icon_tag_traffic, "교통비"))
			add(TagModel(0, R.drawable.ic_check, "체크"))
			add(TagModel(0, R.drawable.ic_backup, "백업"))
			add(TagModel(0, R.drawable.ic_lock, "잠금"))
			add(TagModel(0, R.drawable.ic_chart, "차트"))
			add(TagModel(0, R.drawable.ic_delete, "삭제"))
			add(TagModel(0, R.drawable.ic_home, "홈"))
			add(TagModel(0, R.drawable.ic_calendar, "캘린더"))
		}


		binding.rvTagListContainer.apply {
			setHasFixedSize(true)
			adapter = tagManageAdapter
		}

		val callback = ItemTouchHelperCallback(tagManageAdapter)
		val touchHelper = ItemTouchHelper(callback)
		touchHelper.attachToRecyclerView(binding.rvTagListContainer)
		tagManageAdapter.startDrag(object : TagManageAdapter.OnStartDragListener {
			override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
				touchHelper.startDrag(viewHolder)
			}
		})

		tagManageAdapter.submitList(newList)
	}
}