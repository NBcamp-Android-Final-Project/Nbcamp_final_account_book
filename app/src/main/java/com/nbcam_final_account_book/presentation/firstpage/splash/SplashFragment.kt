package com.nbcam_final_account_book.presentation.firstpage.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.FirstSplashFragmentBinding
import com.nbcam_final_account_book.presentation.firstpage.FirstViewModel
import com.nbcam_final_account_book.presentation.main.MainActivity
import com.nbcam_final_account_book.presentation.template.TemplateActivity
import com.nbcam_final_account_book.presentation.template.TemplateActivity.Companion.EXTRA_TEMPLATE_TYPE
import com.nbcam_final_account_book.presentation.template.TemplateType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private var _binding: FirstSplashFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FirstViewModel by activityViewModels()

    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FirstSplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
    }

    private fun startAnimation() {
        val anim: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_effect)
        val coin01 = binding.splashCoinFirst
        val coin02 = binding.splashCoinSecond
        val coin03 = binding.splashCoinLast
        val delayTime = 2000L

        coin01.visibility = View.VISIBLE
        coin01.startAnimation(anim)

        coin02.visibility = View.VISIBLE
        coin02.startAnimation(anim)

        coin03.visibility = View.VISIBLE
        coin03.startAnimation(anim)

        Handler(Looper.getMainLooper()).postDelayed({
            initView()
        }, delayTime)
    }

    private fun initView() = with(binding) {
        val auth = FirebaseAuth.getInstance()
        val nowCurrentUser = auth.currentUser

        if (nowCurrentUser != null) {
            Log.d("유저", nowCurrentUser.email.toString())
            CoroutineScope(Dispatchers.Main).launch {
                if (isFirstLogin()) {
                    toMainActivity()
                } else {
                    toTemplateActivity()
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