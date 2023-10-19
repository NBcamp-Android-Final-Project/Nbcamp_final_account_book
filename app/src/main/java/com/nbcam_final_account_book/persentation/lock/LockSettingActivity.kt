package com.nbcam_final_account_book.persentation.lock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LockSettingActivityBinding
import com.nbcam_final_account_book.persentation.lock.pin.PinFragment

class LockSettingActivity : AppCompatActivity() {

    private lateinit var binding: LockSettingActivityBinding
    private lateinit var lockSettingFragment: PinFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LockSettingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lockSettingFragment = PinFragment()
        initView()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }

    fun initView() = with(binding) {
        lockRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.lock_btn_none -> {
                    lockDivider1.visibility = View.GONE
                    lockSwitchFingerprint.visibility = View.GONE
                    lockTvPinEdit.visibility = View.GONE
                }
                R.id.lock_btn_locksetting -> {
                    lockDivider1.visibility = View.VISIBLE
                    lockSwitchFingerprint.visibility = View.VISIBLE
                    lockTvPinEdit.visibility = View.VISIBLE

                    loadFragment(lockSettingFragment)
                }
            }
        }

        lockBtnLocksetting.setOnClickListener {
            if (lockSettingFragment.isAdded) {
                loadFragment(lockSettingFragment)
            } else {
                lockSettingFragment = PinFragment()
                loadFragment(lockSettingFragment)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.lock_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}