package com.nbcam_final_account_book.persentation.tag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.TagFragmentBinding


class TagFragment : Fragment() {

	private var _binding: TagFragmentBinding? = null
	private val binding get() = _binding!!

	private val viewModel by lazy {
		ViewModelProvider(
			this@TagFragment,
			TagViewModelFactory(requireActivity())
		)[TagViewModel::class.java]
	}

	lateinit var tagManageAdapter: TagManageAdapter

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
	}

	private fun initView() = with(binding) {

		ivBack.setOnClickListener {
			findNavController().popBackStack()
		}

		ivModify.setOnClickListener {
			val pages = TagPage.NEW
			val action = TagFragmentDirections.actionTagFragmentToEditTagFragment(pages)
			findNavController().navigate(action)
		}
	}

	private fun initViewModel() = with(viewModel) {
		tagList.observe(viewLifecycleOwner) {
			if (it != null) {
//				initTag(getTestListAll())
				Log.d("tagList", getTagListAll().toString())
			}
		}

		initTag()   // 테스트용 (추후 삭제 예정)
	}

	private fun initTag() {

		// 임시 데이터
		val tagList = mutableListOf<TagModel>()
		tagList.apply {
			add(TagModel(0, R.drawable.icon_tag_traffic, "교통비"))
			add(TagModel(0, R.drawable.ic_check, "체크"))
			add(TagModel(0, R.drawable.ic_backup, "백업"))
			add(TagModel(0, R.drawable.ic_lock, "잠금"))
			add(TagModel(0, R.drawable.ic_chart, "차트"))
			add(TagModel(0, R.drawable.ic_delete, "삭제"))
			add(TagModel(0, R.drawable.ic_home, "홈"))
			add(TagModel(0, R.drawable.ic_calendar, "캘린더"))
		}

		tagManageAdapter = TagManageAdapter(
			tagList,
			onItemClick = { position, item -> onItemClickEvent(position, item) })

		binding.rvTagListContainer.apply {
			adapter = tagManageAdapter
			setHasFixedSize(true)
			addItemDecoration(
				DividerItemDecoration(
					requireActivity(),
					LinearLayoutManager.VERTICAL
				)
			)
		}

		val touchHelper = ItemTouchHelper(ItemTouchHelperCallback(tagManageAdapter))
		touchHelper.attachToRecyclerView(binding.rvTagListContainer)

		tagManageAdapter.apply {
			startDrag(object : OnStartDragListener {
				override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
					touchHelper.startDrag(viewHolder)
				}
			})

			deleteItem(object : OnDeleteItemListener {
				override fun onDeleteItem(position: Int, item: MutableList<TagModel>) {
					val dialog = DialogFragment(this@TagFragment, position, item)
					dialog.show(requireActivity().supportFragmentManager, "dialog")
				}
			})
		}
	}

	private fun onItemClickEvent(position: Int, item: TagModel) {
		val pages = TagPage.MODIFY
		val action = TagFragmentDirections.actionTagFragmentToEditTagFragment(pages)
		findNavController().navigate(action)
	}
}

enum class TagPage {
	NEW,
	MODIFY
}