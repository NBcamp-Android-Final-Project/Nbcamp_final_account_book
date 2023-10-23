package com.nbcam_final_account_book.persentation.lock.locksetting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LockSettingFragmentBinding
import com.nbcam_final_account_book.persentation.lock.LockSharedViewModel

class LockSettingFragment : Fragment() {

    private var _binding: LockSettingFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: LockSharedViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LockSettingFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initView()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        locksettingRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.locksetting_btn_none -> {
                    locksettingDivider1.visibility = View.GONE
                    locksettingSwitchFingerprint.visibility = View.GONE
                    locksettingTvPinEdit.visibility = View.GONE
                    sharedViewModel.clearPin()
                }

                R.id.locksetting_btn_pin -> {
                    locksettingDivider1.visibility = View.VISIBLE
                    locksettingSwitchFingerprint.visibility = View.VISIBLE
                    locksettingTvPinEdit.visibility = View.VISIBLE
                }
            }
        }

        locksettingBtnPin.setOnClickListener {
            navController.navigate(R.id.pinFragment)
        }
    }

    private fun initViewModel() {
        sharedViewModel.isPinSet.observe(viewLifecycleOwner) {
            binding.locksettingBtnPin.isChecked = it
        }

        sharedViewModel.pin.observe(viewLifecycleOwner) { pin ->
            Log.d("LockSettingFragment", "pin: $pin")
        }
    }
}