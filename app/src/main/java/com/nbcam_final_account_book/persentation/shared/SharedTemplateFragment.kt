package com.nbcam_final_account_book.persentation.shared

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbcam_final_account_book.data.model.local.TemplateEntity
import com.nbcam_final_account_book.databinding.SharedTemplateFragmentBinding
import com.nbcam_final_account_book.persentation.main.MainViewModel


class SharedTemplateFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    private var _binding: SharedTemplateFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this@SharedTemplateFragment,
            SharedTemplateViewModelFactory()
        )[SharedTemplateViewModel::class.java]
    }

    private val adapter by lazy {
        UserListAdapter { selectedUser ->
            // 현재 템플릿을 가져옴
            val currentTemplate = mainViewModel.getCurrentTemplate()
            val dialog = SharedPostDialog().apply {
                this.selectedUser = selectedUser
                this.currentTemplate = currentTemplate
                listener = object : SharedPostDialog.SharedPostDialogListener {
                    override fun onConfirmSharedTemplate(template: TemplateEntity) {
                        // SharedTemplateViewModel의 sharedTemplate 메서드 호출
                        viewModel.sharedTemplate(selectedUser.key, template)
                    }
                }
            }
            dialog.show(childFragmentManager, "SharedPostDialog")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SharedTemplateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {

        sharedRecyclerUserList.layoutManager = LinearLayoutManager(context)
        sharedRecyclerUserList.adapter = adapter

        sharedItemIvSearch.setOnClickListener {
            val keyword = sharedInputName.text.toString()
            viewModel.setFilter(keyword)
//            viewModel.loadAllUsers()
        }

    }

    private fun initViewModel() {
        with(viewModel) {
            searchResultList.observe(viewLifecycleOwner) { users ->
                adapter.submitList(users)
            }
        }
    }
}