package com.nbcam_final_account_book.persentation.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TagFragmentBinding
import com.nbcam_final_account_book.persentation.entry.ModalTagListAdapter


class TagFragment : Fragment() {

	private var _binding: TagFragmentBinding? = null
	private val binding get() = _binding!!

	private val viewModel by lazy {
		ViewModelProvider(
			this@TagFragment
		)[TagViewModel::class.java]
	}

	private val tagListAdapter by lazy {
		ModalTagListAdapter(onItemClick = { position ->
			onItemClickEvent(position)
		})
	}

	private fun onItemClickEvent(position: Int) {
		Toast.makeText(requireActivity(), "$position 번째 클릭", Toast.LENGTH_SHORT).show()
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
			findNavController().navigate(R.id.action_tagFragment_to_menu_more)
		}
	}

	private fun initViewModel() = with(viewModel) {
		liveDummyTagInTag.observe(viewLifecycleOwner) {

		}
	}

	private fun initTag() {

		// 임시 데이터
		val newList = mutableListOf<Tag>()
		newList.apply {
			add(Tag(R.drawable.ic_tag, "달력"))
			add(Tag(R.drawable.ic_chart, "차트"))
			add(Tag(R.drawable.ic_help, "도움"))
			add(Tag(R.drawable.ic_home, "집"))
			add(Tag(R.drawable.ic_lock, "잠금"))
			add(Tag(R.drawable.ic_more_vert, "수직"))
			add(Tag(R.drawable.ic_mypage, "페이지"))
			add(Tag(R.drawable.ic_backup, "백업"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
			add(Tag(R.drawable.ic_check, "확인"))
		}

		binding.rvTagManageContainer.adapter = tagListAdapter
		tagListAdapter.submitList(newList)
	}
}