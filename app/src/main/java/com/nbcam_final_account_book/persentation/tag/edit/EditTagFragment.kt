package com.nbcam_final_account_book.persentation.tag.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.databinding.EditTagFragmentBinding


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
    }
    private fun initViewModel() = with(viewModel){
        liveDummyTagInEditTag.observe(viewLifecycleOwner){

        }
    }
}