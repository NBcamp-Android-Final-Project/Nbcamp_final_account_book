package com.nbcam_final_account_book.persentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.MyPageFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.FirstActivity
import com.nbcam_final_account_book.persentation.lock.LockActivity
import com.nbcam_final_account_book.persentation.main.MainViewModel

class MyPageFragment : Fragment() {

    private var _binding: MyPageFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, MyPageViewModelFactory(
                requireContext()
            )
        )[MyPageViewModel::class.java]
    }
    private val sharedViewModel : MainViewModel by activityViewModels()
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
        initViewModel()
    }

    private fun initView() = with(binding) {

        mypageIvEdit.setOnClickListener {
            isEditing = !isEditing
            editName()
        }

        mypageTvLock.setOnClickListener {
            val intent = Intent(requireContext(), LockActivity::class.java)
            startActivity(intent)
        }

        mypageTvTag.setOnClickListener {
            findNavController().navigate(R.id.action_menu_more_to_tagFragment)
        }

        mypageTvLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            sharedViewModel.backupDataByLogOut() //백업 테스트 코드
            cleanRoom()
            val intent = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initViewModel() = with(viewModel) {

    }

    private fun cleanRoom() = with(viewModel) {
        cleanRoom()
    }

    private fun editName() = with(binding) {
        if (isEditing) {
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