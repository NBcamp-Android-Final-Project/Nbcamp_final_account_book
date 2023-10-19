package com.nbcam_final_account_book.persentation.lock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nbcam_final_account_book.R
import com.nbcam_final_account_book.databinding.LockActivityBinding
import com.nbcam_final_account_book.persentation.lock.locksetting.LockSettingFragment

class LockActivity : AppCompatActivity() {

    private lateinit var binding: LockActivityBinding
    private lateinit var lockSettingFragment: LockSettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LockActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lockSettingFragment = LockSettingFragment()
        initView()
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
            loadFragment(lockSettingFragment)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.lock_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}