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
import com.nbcam_final_account_book.data.model.local.TagEntity
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


    val tagManageAdapter by lazy {
        TagManageAdapter(
            mutableListOf<TagEntity>(),
            onItemClick = { position, item -> onItemClickEvent(position, item) })
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

        initTag()
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivNewTag.setOnClickListener {
            findNavController().navigate(R.id.action_tagFragment_to_editTagFragment)
        }
    }

    private fun initViewModel() = with(viewModel) {
        liveTagListInEdit.observe(viewLifecycleOwner) {
            if (it != null) {
                    tagManageAdapter.updateList(it)
            }
        }

    }

    private fun initTag() {

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
            startDrag(object : OnDragListener {
                override fun onStartDrag(
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    touchHelper.startDrag(viewHolder)
                }

                override fun onEndDrag(items: MutableList<TagEntity>) {
                    viewModel.tagUpdateInEdit(items)
                }
            })

            deleteItem(object : OnDeleteItemListener {
                override fun onDeleteItem(position: Int, item: MutableList<TagEntity>) {
                    val dialog = DialogFragment(this@TagFragment, position, item)
                    dialog.show(requireActivity().supportFragmentManager, "dialog")
                }
            })
        }
    }

	private fun onItemClickEvent(position: Int, item: TagEntity) {
		val pages = TagPage.MODIFY
		val tagName = item.title

		val action = TagFragmentDirections.actionTagFragmentToEditTagFragment(
			pages = pages,
			tagName = tagName
		)
		findNavController().navigate(action)
	}
}

enum class TagPage {
	NEW,
	MODIFY
}