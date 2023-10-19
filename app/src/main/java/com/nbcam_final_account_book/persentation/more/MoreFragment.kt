package com.nbcam_final_account_book.persentation.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.databinding.MoreFragmentBinding
import com.nbcam_final_account_book.persentation.lock.LockActivity
import com.nbcam_final_account_book.persentation.login.LoginActivity


class MoreFragment : Fragment() {

    private var _binding: MoreFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this, MoreViewModelFactory(
                requireContext()
            )
        )[MoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initView()
    }

    private fun initView() = with(binding) { //레이 아웃 제어
        moreBtnLock.setOnClickListener {
            val intent = Intent(requireContext(), LockActivity::class.java)
            startActivity(intent)
        }


        moreBtnLogout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()

            cleanRoom()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initViewModel() = with(viewModel) {

    }

    private fun cleanRoom() = with(viewModel){
        cleanTemplateListInRoom()
    }
}