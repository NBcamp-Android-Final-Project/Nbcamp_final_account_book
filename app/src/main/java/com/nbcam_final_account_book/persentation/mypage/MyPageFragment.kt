package com.nbcam_final_account_book.persentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding

class MyPageFragment : Fragment() {

    private var _binding: MyPageFragmentBinding? = null
    private val binding get() = _binding!!

//    private val accountBookAdapter = AccountBookAdapter(accountBookList)
//    private val sharedUserAdapter = SharedUserAdapter(sharedUserList)
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        mypageIvEdit.setOnClickListener {
            isEditing = !isEditing
            editName()
        }
    }

    private fun initViewModel() {

    }

    private fun editName() = with(binding) {
        if(isEditing) {
            mypageEtName.isEnabled = true
            mypageEtName.isFocusable = true
            mypageEtName.isFocusableInTouchMode = true
            mypageEtName.requestFocus()
            mypageEtName.setSelection(mypageEtName.text.length)
        } else {
            mypageEtName.isEnabled = false
            mypageEtName.isFocusable = false
            mypageEtName.isFocusableInTouchMode = false
        }
    }
}
