package com.nbcam_final_account_book.persentation.lock.locksetting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.data.sharedprovider.SharedProviderImpl
import com.nbcam_final_account_book.databinding.LockSettingFragmentBinding
import com.nbcam_final_account_book.persentation.lock.LockSharedViewModel
import com.nbcam_final_account_book.persentation.lock.LockViewModelFactory
import com.nbcam_final_account_book.persentation.lock.locksetting.LockSettingViewModel.Companion.NONE
import com.nbcam_final_account_book.persentation.lock.locksetting.LockSettingViewModel.Companion.PIN

class LockSettingFragment : Fragment() {

    private var _binding: LockSettingFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: LockSharedViewModel
    private lateinit var viewModel: LockSettingViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LockSettingFragment", "현재 위치: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("LockSettingFragment", "현재 위치: onCreateView")
        _binding = LockSettingFragmentBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(this, LockViewModelFactory(SharedProviderImpl(requireContext())))[LockSharedViewModel::class.java]
        viewModel = ViewModelProvider(this, LockSettingViewModelFactory(SharedProviderImpl(requireContext())))[LockSettingViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LockSettingFragment", "현재 위치: onViewCreated")
        navController = findNavController()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LockSettingFragment", "현재 위치: onDestroy")
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LockSettingFragment", "현재 위치: onDestroyView")
    }

    private fun initView() = with(binding) {
        locksettingRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.locksetting_btn_none -> {
                    locksettingDivider1.visibility = View.GONE
                    locksettingSwitchFingerprint.visibility = View.GONE
                    locksettingTvPinEdit.visibility = View.GONE
                    viewModel.setLockType(NONE)
//                    sharedViewModel.setIsPinSet(false)
                    sharedViewModel.clearPassword()
                }

                R.id.locksetting_btn_pin -> {
                    locksettingDivider1.visibility = View.VISIBLE
                    locksettingSwitchFingerprint.visibility = View.VISIBLE
                    locksettingTvPinEdit.visibility = View.VISIBLE
                    viewModel.setLockType(PIN)
                }
            }
        }

        locksettingBtnPin.setOnClickListener {
            navController.navigate(R.id.pinFragment)
        }

        sharedViewModel.password.observe(viewLifecycleOwner) { password ->
            Log.d("LockSettingFragment", "password: $password")

            viewModel.selectedLockType.observe(viewLifecycleOwner) { lockType ->
                Log.d("LockSettingFragment", "lockType: $lockType")
                when (lockType) {
                    NONE -> locksettingRadioGroup.check(R.id.locksetting_btn_none)
                    PIN -> {
                        if (password == "") {
                            locksettingRadioGroup.check(R.id.locksetting_btn_none)
                        } else {
                            locksettingRadioGroup.check(R.id.locksetting_btn_pin)
                        }
                    }
                }
            }
        }
    }
}