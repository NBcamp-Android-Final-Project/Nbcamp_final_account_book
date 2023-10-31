package com.nbcam_final_account_book.persentation.firstpage.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstSplashFragmentBinding
import com.nbcam_final_account_book.persentation.firstpage.LoginViewModel
import com.nbcam_final_account_book.persentation.main.MainActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity
import com.nbcam_final_account_book.persentation.template.TemplateActivity.Companion.EXTRA_TEMPLATE_TYPE
import com.nbcam_final_account_book.persentation.template.TemplateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private var _binding: FirstSplashFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstSplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {

        //TODO viewmodel 수준으로 옮기기
        val auth = FirebaseAuth.getInstance()
        val nowCurrentUser = auth.currentUser

        if (nowCurrentUser != null) {
            Log.d("유저", nowCurrentUser.email.toString())
            CoroutineScope(Dispatchers.Main).launch {
                if (isFirstLogin()) {
                    toMainActivity()
                } else {
                    toTemplateActivity() //최초 로그인 시 템플릿으로 이동
                }
            }
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

    //Todo 작명 바꾸기
    private suspend fun isFirstLogin(): Boolean {
        val data = viewModel.getAllTemplateSize()
        Log.d("listsize", data.toString())

        return data > 0
    }

    private fun toMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun toTemplateActivity() {
        val intent = Intent(requireContext(), TemplateActivity::class.java).apply {
            putExtra(EXTRA_TEMPLATE_TYPE, TemplateType.NEW.name)
        }
        startActivity(intent)
        requireActivity().finish()
    }


}